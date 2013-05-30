package add.exam.poll.controllers;

import add.exam.common.services.CommonService;
import add.exam.exam.controllers.ExamController;
import add.exam.model.poll.Poll;
import add.exam.model.poll.PollQuestion;
import add.exam.model.poll.PollQuestionAnswer;
import add.exam.model.user.User;
import add.exam.poll.helpers.PollQuestionHelper;
import add.exam.poll.services.PollService;
import add.exam.user.services.LoginService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import javax.inject.Inject;

@Controller
@RequestMapping(PollQuestionController.REQUEST_MAPPING)
public class PollQuestionController
{
    public static final String REQUEST_MAPPING = "/poll/{poll_id}/question";
    public static final String REDIRECT_TO_EDIT_QUESTION_URL = "redirect:/poll/%s/question/%s/edit.html";


    private static final String QUESTION_FORM_TEMPLATE = "poll/questionForm";
    private static final String QUESTION_STATUS_TEMPLATE = "common/ajax-templates/questionStatus";

    @Inject
    private PollService pollService;

    @Inject
    private CommonService commonService;

    @Inject
    private LoginService userService;

    @Inject
    private PollQuestionHelper helper;

    @RequestMapping(value = "/new", method = RequestMethod.GET)
    public String newPollQuestion(Model model, @PathVariable("poll_id") Integer pollId){
        PollQuestion question = new PollQuestion();
        User user = userService.getUser();
        Poll poll = pollService.getPoll(pollId, user.getId());
        question.setPoll(poll);
        helper.addQuestionToModel(question, model);
        return QUESTION_FORM_TEMPLATE;
    }

    @RequestMapping(value = "/{id}/edit", method = RequestMethod.GET)
    public String editPollQuestion(Model model, @PathVariable("poll_id") Integer pollId, @PathVariable Integer id){
        User user = userService.getUser();
        Poll poll = pollService.getPoll(pollId, user.getId());

        PollQuestion question = pollService.getPollQuestionWithAnswers(id, pollId, user.getId());
        //checks if question can be edited
        if (poll.getPublished()){
            return String.format(PollController.REDIRECT_TO_POLL_RESULT_URL, pollId);
        }
        helper.addQuestionToModel(question, model);
        question.setCompleted(question.getAnswers().size() > 1);
        pollService.save(question);
        return QUESTION_FORM_TEMPLATE;
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String savePollQuestion(@PathVariable("poll_id") Integer pollId, @ModelAttribute PollQuestion question){
        User user = userService.getUser();
        Poll poll = pollService.getPoll(pollId, user.getId());
        question.setPoll(poll);
        pollService.save(question);
        return String.format(REDIRECT_TO_EDIT_QUESTION_URL, pollId, question.getId());
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public String delete(@PathVariable ("id") Integer questionId){
        commonService.delete(PollQuestion.class, questionId);
        return ExamController.AJAX_SUCCESS_MESSAGE_TEMPLATE;
    }


    @RequestMapping(value = "{question_id}/answer/save", method = RequestMethod.POST)
    public String saveAnswer(@PathVariable("poll_id") Integer pollId,
                       @PathVariable ("question_id") Integer questionId,
                       @ModelAttribute PollQuestionAnswer answer){
        User user = userService.getUser();
        PollQuestion question = pollService.getPollQuestionWithAnswers(questionId, pollId, user.getId());
        answer.setQuestion(question);
        pollService.save(answer);
        return String.format(PollQuestionController.REDIRECT_TO_EDIT_QUESTION_URL, pollId, questionId);
    }

    @RequestMapping(value = "/{question_id}/answer/{id}", method = RequestMethod.DELETE)
    public String delete(@PathVariable("poll_id") Integer pollId,
                        @PathVariable ("question_id") Integer questionId,
                        @PathVariable ("id") Integer answerId, Model model){
        User user = userService.getUser();
        PollQuestion question = pollService.getPollQuestionWithAnswers(questionId, pollId, user.getId());
        commonService.delete(PollQuestionAnswer.class, answerId);
        if (question.getCompleted() && question.getAnswers().size() < 2){
            question.setCompleted(false);
            pollService.save(question);
        }
        helper.addQuestionToModel(question, model);
        return ExamController.AJAX_SUCCESS_MESSAGE_TEMPLATE;
    }

}
