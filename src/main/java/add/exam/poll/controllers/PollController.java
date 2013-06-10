package add.exam.poll.controllers;

import add.exam.common.services.CommonService;
import add.exam.model.poll.Poll;
import add.exam.model.poll.PollQuestion;
import add.exam.model.poll.PollQuestionAnswer;
import add.exam.model.user.User;
import add.exam.poll.helpers.PollHelper;
import add.exam.poll.services.PollService;
import add.exam.user.services.LoginService;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.inject.Inject;

@Controller
@RequestMapping(PollController.REQUEST_MAPPING)
public class PollController
{
    public static final String REQUEST_MAPPING = "/poll";
    public static final String REDIRECT_TO_EDIT_POLL_URL = "redirect:/poll/%s/edit.html";
    public static final String REDIRECT_TO_POLL_RESULT_URL = "redirect:/poll/%s/result.html";

    //view templates
    private static final String POLL_FORM_TEMPLATE = "poll/pollForm";
    private static final String POLL_RESULT_NOT_READY_TEMPLATE = "poll/pollResultNotReady";
    private static final String POLL_RESULT_TEMPLATE = "poll/pollResult";

    @Inject
    private PollService pollService;

    @Inject
    private LoginService userService;

    @Inject
    private CommonService commonService;

    @Inject
    private PollHelper helper;

    @RequestMapping(value = "/{id}/edit", method = RequestMethod.GET)
    public String editPoll(Model model, @PathVariable Integer id){
        User user = userService.getUser();
        Poll poll = pollService.getPollWithQuestions(id, user.getId());
        if (poll.getPublished()){
            return String.format(REDIRECT_TO_POLL_RESULT_URL, poll.getId());
        }
        helper.addPollToModel(poll, model);
        return POLL_FORM_TEMPLATE;
    }

    @RequestMapping(value = "/new", method = RequestMethod.GET)
    public String newPoll(Model model){
        helper.addPollToModel(new Poll(), model);
        return POLL_FORM_TEMPLATE;
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String savePoll(@ModelAttribute Poll poll){
        User user = userService.getUser();
        poll.setUser(user);
        pollService.save(poll);
        return String.format(REDIRECT_TO_EDIT_POLL_URL, poll.getId());
    }

    @RequestMapping(value = "/{pollId}/publish", method = RequestMethod.POST)
    public String publishPoll(@PathVariable Integer pollId){
        User user = userService.getUser();
        Poll poll = pollService.getPollWithQuestions(pollId, user.getId());
        poll.setPublished(true);
        pollService.save(poll);
        for (PollQuestion question: poll.getQuestions()){
            if (!question.getCompleted()){
                commonService.delete(PollQuestion.class, question.getId());
            }
        }
        return String.format(REDIRECT_TO_POLL_RESULT_URL, poll.getId());
    }

    @RequestMapping(value="/{pollId}/result", method = RequestMethod.GET)
    public String pollResults(@PathVariable Integer pollId, Model model){
        User user = userService.getUser();
        Poll poll = commonService.get(Poll.class, pollId);
        switch(user.getRole()){
            case ROLE_STUDENT:
                boolean accessAllowed = pollService.isAccessAllowed(user.getId(), pollId);
                if (!accessAllowed){
                    throw new AccessDeniedException("This user can't view this poll");
                }
                if (!poll.getCompleted()){
                    helper.addPoll(poll, model);
                    return POLL_RESULT_NOT_READY_TEMPLATE;
                }
                break;
            case ROLE_TEACHER:
                if (!poll.getUser().equals(user)){
                    throw new AccessDeniedException("This user can't view this poll");
                }
        }
        poll.setQuestions(pollService.getPollQuestionsAndAnswers(pollId));
        //remove next loop when thymeleaf version 2.0.17 will be ready,bug 155
        for (PollQuestion question: poll.getQuestions()){
            for (PollQuestionAnswer answer: question.getAnswers()){
                answer.setVoted(answer.getVoted() * 100);
            }
        }
        helper.addPoll(poll, model);
        return POLL_RESULT_TEMPLATE;

    }
}
