package add.exam.poll.controllers;

import add.exam.common.services.CommonService;
import add.exam.model.poll.Poll;
import add.exam.model.poll.PollQuestion;
import add.exam.model.poll.PollQuestionAnswer;
import add.exam.model.user.User;
import add.exam.poll.helpers.PollHelper;
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
    private static final String DELETE_POLL_QUESTION_AJAX_TEMPLATE = "common/ajax-templates/deletePollQuestion";
    private static final String DELETE_POLL_ANSWER_AJAX_TEMPLATE = "common/ajax-templates/deletePollAnswer";

    @Inject
    private PollService pollService;

    @Inject
    private CommonService commonService;

    @Inject
    private LoginService userService;

    @Inject
    private PollQuestionHelper helper;

    @Inject
    private PollHelper pollHelper;

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
    public String delete(@PathVariable ("id") Integer questionId, @PathVariable("poll_id") Integer pollId, Model model){
        User user = userService.getUser();
        commonService.delete(PollQuestion.class, questionId);
        Poll poll = pollService.getPollWithQuestions(pollId, user.getId());
        pollHelper.addNotCompletedPollAttr(poll, model);
        return DELETE_POLL_QUESTION_AJAX_TEMPLATE;
    }


    @RequestMapping(value = "{question_id}/answer/save", method = RequestMethod.POST)
    public String saveAnswer(@PathVariable("poll_id") Integer pollId,
                       @PathVariable ("question_id") Integer questionId,
                       @ModelAttribute PollQuestionAnswer answer){
        User user = userService.getUser();
        PollQuestion question = pollService.getPollQuestionWithAnswers(questionId, pollId, user.getId());
        if (answer.getId() == null){
            question.setCompleted(question.getAnswers().size() > 0);
        }else{
            question.setCompleted(question.getAnswers().size() > 1);
        }
        pollService.save(question);
        answer.setQuestion(question);
        pollService.save(answer);
        return String.format(PollQuestionController.REDIRECT_TO_EDIT_QUESTION_URL, pollId, questionId);
    }

    @RequestMapping(value = "/{question_id}/answer/{answerId}", method = RequestMethod.DELETE)
    public String delete(@PathVariable("poll_id") Integer pollId,
                        @PathVariable ("question_id") Integer questionId,
                        @PathVariable Integer answerId, Model model){
        User user = userService.getUser();
        PollQuestion question = pollService.getPollQuestionWithAnswers(questionId, pollId, user.getId());
        PollQuestionAnswer answer = commonService.get(PollQuestionAnswer.class, answerId);
        question.setCompleted(question.getAnswers().size() > 2);
        question.getAnswers().remove(answer);
        pollService.save(question);
        commonService.delete(PollQuestionAnswer.class, answerId);
        helper.addQuestionToModel(question, model);
        return DELETE_POLL_ANSWER_AJAX_TEMPLATE;
    }

}
