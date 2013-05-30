package add.exam.poll.controllers;

import add.exam.model.poll.Poll;
import add.exam.model.user.User;
import add.exam.poll.helpers.PollHelper;
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
@RequestMapping(PollController.REQUEST_MAPPING)
public class PollController
{
    public static final String REQUEST_MAPPING = "/poll";
    public static final String REDIRECT_TO_EDIT_POLL_URL = "redirect:/poll/%s/edit.html";
    public static final String REDIRECT_TO_POLL_RESULT_URL = "redirect:/poll/%s/result.html";

    //view templates
    private static final String POLL_FORM_TEMPLATE = "poll/pollForm";

    @Inject
    private PollService pollService;

    @Inject
    private LoginService userService;

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
}
