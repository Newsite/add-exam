package add.exam.exam.controllers;

import add.exam.common.services.CommonService;
import add.exam.exam.helpers.ExamQuestionHelper;
import add.exam.exam.services.ExamService;
import add.exam.model.exam.Exam;
import add.exam.model.exam.ExamQuestion;
import add.exam.model.exam.ExamQuestionAnswer;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.inject.Inject;
import java.util.List;

@Controller
@RequestMapping(ExamQuestionController.REQUEST_MAPPING)
public class ExamQuestionController
{
    public static final String REQUEST_MAPPING = "/exam/{exam_id}/question";
    public static final String REDIRECT_TO_EDIT_QUESTION_URL = "redirect:/exam/%s/question/%s/edit.html";


    private static final String QUESTION_FORM_TEMPLATE = "exam/questionForm";
    private static final String QUESTION_STATUS_TEMPLATE = "common/ajax-templates/questionStatus";

    @Inject
    private ExamService examService;

    @Inject
    private CommonService commonService;

    @Inject
    private ExamQuestionHelper helper;

    @RequestMapping(value = "/new", method = RequestMethod.GET)
    public String newExamQuestion(Model model, @PathVariable("exam_id") Integer examId){
        ExamQuestion question = new ExamQuestion();
        Exam exam = commonService.get(Exam.class, examId);
        question.setExam(exam);
        helper.addQuestionToModel(question, model);
        return QUESTION_FORM_TEMPLATE;
    }

    @RequestMapping(value = "/{id}/edit", method = RequestMethod.GET)
    public String editExamQuestion(Model model, @PathVariable Integer id){
        ExamQuestion question = commonService.get(ExamQuestion.class, id);
        //checks if question can be edited
        if (examService.isExamEditingClosed(question.getExam())){
            return String.format(ExamController.REDIRECT_TO_EDIT_EXAM_URL, question.getExam().getId());
        }
        List<ExamQuestionAnswer> answers = examService.getExamQuestionAnswers(id);
        helper.editQuestion(question, answers, model);
        return QUESTION_FORM_TEMPLATE;
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String saveExamQuestion(@PathVariable("exam_id") Integer examId, @ModelAttribute ExamQuestion question){
        Exam exam = commonService.get(Exam.class, examId);
        question.setExam(exam);
        if (question.getId() != null){
            List<ExamQuestionAnswer> answers = examService.getExamQuestionAnswers(question.getId());
            boolean isCompleted = helper.isQuestionCompleted(answers, question.getType());
            question.setCompleted(isCompleted);
        }
        examService.save(question);
        examService.setExamPublished(question.getExam());
        return String.format(REDIRECT_TO_EDIT_QUESTION_URL, question.getExam().getId(), question.getId());
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public String delete(@PathVariable ("id") Integer questionId){
        commonService.delete(ExamQuestion.class, questionId);
        return ExamController.AJAX_SUCCESS_MESSAGE_TEMPLATE;
    }

    @RequestMapping(value = "/{id}/status", method = RequestMethod.POST)
    public String status(@PathVariable ("id") Integer questionId, Model model){
        ExamQuestion question = commonService.get(ExamQuestion.class, questionId);
        List<ExamQuestionAnswer> answers = examService.getExamQuestionAnswers(questionId);
        helper.addQuestionStatusAttributes(question, answers, model);
        return QUESTION_STATUS_TEMPLATE;
    }

}
