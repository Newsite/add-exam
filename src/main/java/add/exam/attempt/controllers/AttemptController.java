package add.exam.attempt.controllers;

import add.exam.attempt.helpers.AttemptHelper;
import add.exam.attempt.forms.AttemptQuestionForm;
import add.exam.attempt.services.AttemptService;
import add.exam.common.services.CommonService;
import add.exam.exam.controllers.ExamController;
import add.exam.model.attempt.Attempt;
import add.exam.model.attempt.AttemptQuestion;
import add.exam.model.exam.Exam;
import add.exam.model.user.User;
import add.exam.user.services.LoginService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.inject.Inject;
import java.util.List;

@Controller
public class AttemptController
{
    private static final String REQUEST_MAPPING_NEW_ATTEMPT = "/exam/{exam_id}/attempt/new";
    private static final String REDIRECT_TO_VIEW_ATTEMPT = "redirect:/attempt/%s.html";

    //view templates
    private static final String VIEW_TEMPLATE = "attempt/view";
    private static final String RESULT_TEMPLATE = "attempt/result";
    private static final String MARKED_LIST_TEMPLATE = "attempt/markedList";
    private static final String AJAX_SUCCESS_MESSAGE_TEMPLATE = "common/ajax-templates/success";

    @Inject
    private AttemptService attemptService;

    @Inject
    private CommonService commonService;

    @Inject
    private LoginService userService;

    @Inject
    private AttemptHelper helper;

    @RequestMapping(value = REQUEST_MAPPING_NEW_ATTEMPT, method = RequestMethod.GET)
    public String newAttempt(@PathVariable ("exam_id") Integer examId){
        User user = userService.getUser();
        Exam exam = commonService.get(Exam.class, examId);
        if(!exam.getPublished()){
            return String.format(ExamController.REDIRECT_TO_EXAM_VIEW_URL, exam.getId());
        }
        Attempt attempt = new Attempt();
        attempt.setExam(exam);
        if (user != null){
            attempt.setUser(user);
        }
        List<AttemptQuestion> questions = attemptService.attachAttemptQuestions(attempt);
        attempt.setQuestions(questions);
        attemptService.create(attempt);
        return String.format(REDIRECT_TO_VIEW_ATTEMPT, attempt.getId());
    }

    @RequestMapping(value = "/attempt/{id}", method = RequestMethod.GET)
    public String viewAttempt(Model model, @PathVariable Integer id){
        Attempt attempt = commonService.get(Attempt.class, id);
        if (attempt.getCompleted()){
            helper.showAttemptResult(attempt, model);
            return RESULT_TEMPLATE;
        }
        AttemptQuestion question = attemptService.getCurrentQuestion(id, attempt.getCurrentQuestionNumber());
        AttemptQuestionForm form = new AttemptQuestionForm();
        form.setAttemptQuestion(question);
        form.setAnswers(attemptService.getUserAnswer(question));
        commonService.update(question);
        helper.viewQuestion(form, attempt, question, model);
        return VIEW_TEMPLATE;
    }

    @RequestMapping(value = "/attempt/{id}/next", method = RequestMethod.GET)
    public String nextQuestion(@PathVariable Integer id){
        Attempt attempt = commonService.get(Attempt.class, id);
        attempt.setCurrentQuestionNumber(attempt.getCurrentQuestionNumber() + 1);
        commonService.update(attempt);
        return String.format(REDIRECT_TO_VIEW_ATTEMPT, id);
    }

    @RequestMapping(value = "/attempt/{id}/previous", method = RequestMethod.GET)
    public String previousQuestion(@PathVariable Integer id){
        Attempt attempt = commonService.get(Attempt.class, id);
        attempt.setCurrentQuestionNumber(attempt.getCurrentQuestionNumber() - 1);
        commonService.update(attempt);
        return String.format(REDIRECT_TO_VIEW_ATTEMPT, id);
    }

    @RequestMapping(value = "/attempt/{id}/save", method = RequestMethod.POST)
    public String saveAnswer(@PathVariable Integer id, @ModelAttribute AttemptQuestionForm form){
        Attempt attempt = commonService.get(Attempt.class, id);
        AttemptQuestion question = attemptService.getCurrentQuestion(id, attempt.getCurrentQuestionNumber());
        attemptService.saveUserAnswer(form.getAnswers(), form.getAnswerNumber(), question);
        return AJAX_SUCCESS_MESSAGE_TEMPLATE;
    }

    @RequestMapping(value = "/attempt/mark/{question_id}", method = RequestMethod.POST)
    public String markQuestion(@PathVariable("question_id") Integer questionId,
                               @ModelAttribute("marked") Boolean marked){
        AttemptQuestion question = commonService.get(AttemptQuestion.class, questionId);
        question.setMarked(marked);
        commonService.update(question);
        return AJAX_SUCCESS_MESSAGE_TEMPLATE;
    }

    @RequestMapping(value = "/attempt/{id}/finish", method = RequestMethod.GET)
    public String finish(@PathVariable Integer id){
        Attempt attempt = commonService.get(Attempt.class, id);
        List<AttemptQuestion> questions = attemptService.getAttemptQuestions(id);
        attempt.setQuestions(questions);
        if (!attempt.getCompleted()){
            attemptService.finish(attempt);
        }
        return String.format(REDIRECT_TO_VIEW_ATTEMPT, id);
    }

    @RequestMapping(value = "/attempt/{id}/marked", method = RequestMethod.GET)
    public String viewMarkedList(Model model, @PathVariable Integer id){
        Attempt attempt = commonService.get(Attempt.class, id);
        if (attempt.getCompleted()){
            return String.format(REDIRECT_TO_VIEW_ATTEMPT, id);
        }
        List<AttemptQuestion> attemptQuestions = attemptService.getAttemptQuestions(id);
        List<Integer> questions = attemptService.getMarkedQuestions(attemptQuestions);
        helper.addMarkedListAttributes(model, questions, attempt);
        return MARKED_LIST_TEMPLATE;
    }

    @RequestMapping(value = "/attempt/{id}/{question_id}", method = RequestMethod.GET)
    public String returnToQuestion(@PathVariable Integer id, @PathVariable("question_id") Integer questionId){
        Attempt attempt = commonService.get(Attempt.class, id);
        attempt.setCurrentQuestionNumber(questionId - 1);
        commonService.update(attempt);
        return String.format(REDIRECT_TO_VIEW_ATTEMPT, id);
    }

}
