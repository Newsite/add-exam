package add.exam.poll.helpers;

import add.exam.model.poll.Poll;
import add.exam.model.poll.PollQuestion;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

@Component
public class PollHelper
{
    private static final Integer QUESTION_COUNT_MAX_LIMIT = 12;

    //attribute names
    private static final String POLL = "poll";
    private static final String NOT_COMPLETED_POLL = "notCompletedPoll";
    private static final String SHOW_ADD_QUESTION_BUTTON = "showAddQuestionButton";

    public void addPollToModel(Poll poll, Model model){
        addPoll(poll, model);
        addNotCompletedPollAttr(poll, model);
        if (poll.getQuestions() == null || poll.getQuestions().size() < QUESTION_COUNT_MAX_LIMIT){
            model.addAttribute(SHOW_ADD_QUESTION_BUTTON, true);
        }
    }

    public void addPoll(Poll poll, Model model){
        model.addAttribute(POLL, poll);
    }

    public void addNotCompletedPollAttr(Poll poll, Model model){
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
