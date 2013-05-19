package add.exam.exam.controllers;

import add.exam.exam.helpers.ExamHelper;
import add.exam.common.services.CommonService;
import add.exam.exam.services.ExamService;
import add.exam.model.exam.Exam;
import add.exam.model.exam.ExamQuestion;
import add.exam.model.user.User;
import add.exam.user.services.LoginService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.util.List;

@Controller
@RequestMapping(ExamController.REQUEST_MAPPING)
public class ExamController
{
    public static final String REQUEST_MAPPING = "/exam";
    public static final String REDIRECT_TO_EDIT_EXAM_URL = "redirect:/exam/%s/edit.html";
    public static final String REDIRECT_TO_EXAM_VIEW_URL = "redirect:/exam/%s.html";

    //view templates
    private static final String EXAM_VIEW_TEMPLATE = "exam/view";
    private static final String EXAM_RULES_TEMPLATE = "exam/rules";
    private static final String EXAM_FORM_TEMPLATE = "exam/examForm";
    private static final String CLOSED_EXAM_EDITING_TEMPLATE = "exam/closedExamEditing";
    private static final String EXAM_STATUS_TEMPLATE = "common/ajax-templates/examStatus";
    public static final String AJAX_SUCCESS_MESSAGE_TEMPLATE = "common/ajax-templates/success";

    @Inject
    private ExamService examService;

    @Inject
    private CommonService commonService;

    @Inject
    private LoginService userService;

    @Inject
    private ExamHelper helper;

    @RequestMapping(value = "/{id}/rules", method = RequestMethod.GET)
    public String viewExamRules(@PathVariable ("id") Integer examId, Model model){
        Exam exam = commonService.get(Exam.class, examId);
        if(!exam.getPublished()){
            return String.format(REDIRECT_TO_EXAM_VIEW_URL, exam.getId());
        }
        helper.addExamToModel(exam, model);
        return EXAM_RULES_TEMPLATE;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String examView(Model model, @PathVariable Integer id){
        Exam exam = commonService.get(Exam.class, id);
        helper.addExamToModel(exam, model);
        helper.addTakeExamErrors(exam, model);
        return EXAM_VIEW_TEMPLATE;
    }

    @RequestMapping(value = "/{id}/edit", method = RequestMethod.GET)
    public String editExam(Model model, @PathVariable Integer id){
        User user = userService.getUser();
        Exam exam = examService.getExam(id, user.getId());
        if (examService.isExamEditingClosed(exam)){
            Long activeAttemptsNumber = examService.getActiveAttemptsNumber(id);
            helper.addClosedExamEditingErrors(exam, activeAttemptsNumber, model);
            return CLOSED_EXAM_EDITING_TEMPLATE;
        }
        List<ExamQuestion> questions = examService.getExamQuestions(id);
        helper.addEditExamDataToModel(exam, questions, examService.isExamCompleted(exam,questions), model);
        return EXAM_FORM_TEMPLATE;
    }

    @RequestMapping(value = "/new", method = RequestMethod.GET)
    public String newExam(Model model){
        helper.addExamAndTypesToModel(new Exam(), model);
        return EXAM_FORM_TEMPLATE;
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String saveExam(@ModelAttribute Exam exam){
        User user = userService.getUser();
        exam.setUser(user);
        examService.save(exam);
        return String.format(REDIRECT_TO_EDIT_EXAM_URL, exam.getId());
    }

    @RequestMapping(value = "/{id}/unpublish", method = RequestMethod.POST)
    public String unpublishExam(@PathVariable Integer id){
        User user = userService.getUser();
        Exam exam = examService.getExam(id, user.getId());
        exam.setPublished(false);
        examService.save(exam);
        return String.format(REDIRECT_TO_EDIT_EXAM_URL, exam.getId());
    }

    @RequestMapping(value = "/{id}/status", method = RequestMethod.POST)
    public String status(@PathVariable Integer id, Model model){
        User user = userService.getUser();
        Exam exam = examService.getExam(id, user.getId());
        List<ExamQuestion> questions = examService.getExamQuestions(id);
        boolean isCompleted = examService.isExamCompleted(exam, questions);
        helper.addCompletedExamAttributes(isCompleted, model);
        return EXAM_STATUS_TEMPLATE;
    }
}
