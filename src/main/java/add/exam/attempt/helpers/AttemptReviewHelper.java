package add.exam.attempt.helpers;

import add.exam.attempt.model.UserAnswer;
import add.exam.model.attempt.AttemptQuestion;
import com.google.common.base.Objects;
import com.google.common.base.Strings;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import java.util.List;

@Component
public class AttemptReviewHelper
{
    //model attributes
    private static final String FIRST_QUESTION = "firstQuestion";
    private static final String LAST_QUESTION = "lastQuestion";
    private static final String ANSWERS = "answers";
    private static final String QUESTION = "question";
    private static final String CURRENT_QUESTION_NUMBER = "currentQuestionNumber";
    private static final String SHOW_EXPLANATION_BLOCK = "showExplanation";

    public void addReviewAttr(Integer questionNumber, AttemptQuestion question, List<UserAnswer> answers, Model model){
        if (questionNumber == 1){
            model.addAttribute(FIRST_QUESTION, true);
        }
        if (Objects.equal(questionNumber, question.getAttempt().getExam().getQuestionsCount())){
            model.addAttribute(LAST_QUESTION, true);
        }
        model.addAttribute(QUESTION, question);
        model.addAttribute(ANSWERS, answers);
        model.addAttribute(CURRENT_QUESTION_NUMBER, questionNumber);
        boolean showExplanation = question.getAttempt().getExam().getSettings().getShowAnswers()
                && !Strings.isNullOrEmpty(question.getExplanation());
        model.addAttribute(SHOW_EXPLANATION_BLOCK, showExplanation);
        //attribute that is responsible for question type
        model.addAttribute(question.getType().toString(), true);
    }
}
