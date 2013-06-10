package add.exam.poll.helpers;

import add.exam.model.poll.PollQuestion;
import add.exam.model.poll.PollQuestionAnswer;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

@Component
public class PollQuestionHelper
{
    private static final Integer ANSWER_COUNT_MAX_LIMIT = 8;

    //model attributes
    private static final String QUESTION = "question";
    private static final String ANSWER = "answer";
    private static final String SHOW_ADD_ANSWER_BUTTON = "showAddAnswerButton";
    private static final String NOT_COMPLETED_QUESTION = "notCompletedQuestion";

    public void addQuestionToModel(PollQuestion question, Model model){
        model.addAttribute(QUESTION, question);
        model.addAttribute(ANSWER, new PollQuestionAnswer());
        if (question.getAnswers() == null || question.getAnswers().size() < ANSWER_COUNT_MAX_LIMIT){
            model.addAttribute(SHOW_ADD_ANSWER_BUTTON, true);
        }
        addQuestionStatusToModel(question, model);
    }

    private void addQuestionStatusToModel(PollQuestion question, Model model){
        if (question.getAnswers() == null || question.getAnswers().size() < 2){
            model.addAttribute(NOT_COMPLETED_QUESTION, true);
        }
    }
}
