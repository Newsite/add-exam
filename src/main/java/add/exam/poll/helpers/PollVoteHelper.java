package add.exam.poll.helpers;

import add.exam.model.poll.Poll;
import add.exam.model.poll.PollQuestion;
import add.exam.model.poll.PollVote;
import add.exam.poll.model.VoteForm;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

@Component
public class PollVoteHelper
{
    //attributes
    private static final String VOTE = "vote";
    private static final String QUESTION = "question";
    private static final String QUESTION_COUNT = "questionsCount";
    private static final String VOTE_FORM = "voteForm";


    public void addVoteAttr(PollVote vote, Poll poll, PollQuestion question, Model model)
    {
        model.addAttribute(VOTE, vote);
        model.addAttribute(QUESTION, question);
        model.addAttribute(QUESTION_COUNT, poll.getQuestions().size());
        VoteForm voteForm = new VoteForm();
        voteForm.setQuestionId(question.getId());
        model.addAttribute(VOTE_FORM, voteForm);
    }
}
