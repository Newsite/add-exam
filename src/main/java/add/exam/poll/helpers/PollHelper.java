package add.exam.poll.helpers;

import add.exam.model.poll.Poll;
import add.exam.model.poll.PollQuestion;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

@Component
public class PollHelper
{
    //attribute names
    private static final String POLL = "poll";
    private static final String NOT_COMPLETED_POLL = "notCompletedPoll";

    public void addPollToModel(Poll poll, Model model){
        model.addAttribute(POLL, poll);
        addNotCompletedPollAttr(poll, model);
    }

    private void addNotCompletedPollAttr(Poll poll, Model model){
        if (poll.getQuestions() == null || poll.getQuestions().isEmpty()){
            model.addAttribute(NOT_COMPLETED_POLL, true);
            return;
        }
        for (PollQuestion question: poll.getQuestions()){
            if(question.getCompleted()){
                return;
            }
        }
        model.addAttribute(NOT_COMPLETED_POLL, true);
    }

}
