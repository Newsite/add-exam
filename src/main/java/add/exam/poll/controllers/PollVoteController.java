package add.exam.poll.controllers;

import add.exam.common.services.CommonService;
import add.exam.exceptions.ResourceNotFoundException;
import add.exam.model.poll.Poll;
import add.exam.model.poll.PollQuestion;
import add.exam.model.poll.PollQuestionAnswer;
import add.exam.model.poll.PollVote;
import add.exam.model.user.User;
import add.exam.model.user.UserRole;
import add.exam.poll.helpers.PollVoteHelper;
import add.exam.poll.model.VoteForm;
import add.exam.poll.services.PollService;
import add.exam.poll.services.PollVoteService;
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
public class PollVoteController
{
    private static final String REQUEST_MAPPING_VOTE = "/poll/{id}";
    private static final String REDIRECT_TO_VOTE = "redirect:/poll/%s.html";

    //templates
    private static final String VOTE_TEMPLATE = "poll/voteForm";

    @Inject
    private PollVoteHelper helper;

    @Inject
    private PollService pollService;

    @Inject
    private CommonService commonService;

    @Inject
    private PollVoteService pollVoteService;

    @Inject
    private LoginService userService;

    @RequestMapping(value = REQUEST_MAPPING_VOTE, method = RequestMethod.GET)
    public String vote(@PathVariable Integer id, Model model){
        User user = userService.getUser();
        Poll poll = pollService.getPollWithQuestions(id);
        if (!poll.getPublished()){
            throw new ResourceNotFoundException("Poll is not found!");
        }
        if (user.getRole() == UserRole.ROLE_TEACHER){
            return String.format(PollController.REDIRECT_TO_POLL_RESULT_URL, poll.getId());
        }
        PollVote vote = pollVoteService.getVote(user.getId(), poll.getId());
        if (vote == null){
            boolean accessAllowed = pollService.isAccessAllowed(user.getId(), id);
            if (!accessAllowed){
                throw new AccessDeniedException("This user can't vote in this poll");
            }
            vote = new PollVote();
            vote.setPoll(poll);
            vote.setUser(user);
            pollVoteService.save(vote);
        }
        //check if vote is completed
        if (vote.getCompleted() ){
            return String.format(PollController.REDIRECT_TO_POLL_RESULT_URL, poll.getId());
        }
        PollQuestion question = poll.getQuestions().get(vote.getCurrentQuestionNumber());
        question = pollService.getPollQuestionWithAnswers(question.getId());
        helper.addVoteAttr(vote, poll, question, model);
        return VOTE_TEMPLATE;
    }

    @RequestMapping(value = REQUEST_MAPPING_VOTE, method = RequestMethod.POST)
    public String saveVote(@PathVariable Integer id, @ModelAttribute VoteForm form){
        User user  = userService.getUser();
        PollVote vote = pollVoteService.getVote(user.getId(), id);
        Poll poll = pollService.getPollWithQuestions(id);
        PollQuestionAnswer answer = commonService.get(PollQuestionAnswer.class, form.getAnswerId());
        answer.setVoted(answer.getVoted() + 1);
        pollService.save(answer);
        Integer questionNumber = vote.getCurrentQuestionNumber() + 1;
        if (questionNumber == poll.getQuestions().size()){
            vote.setCompleted(true);
        }
        vote.setCurrentQuestionNumber(questionNumber);
        pollVoteService.save(vote);
        return String.format(REDIRECT_TO_VOTE, id);
    }

}
