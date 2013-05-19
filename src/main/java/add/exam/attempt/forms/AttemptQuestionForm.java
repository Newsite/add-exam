package add.exam.attempt.forms;

import add.exam.attempt.model.UserAnswer;
import add.exam.model.attempt.AttemptQuestion;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class AttemptQuestionForm
{
    @Getter
    @Setter
    private AttemptQuestion attemptQuestion;

    @Getter
    @Setter
    private List<UserAnswer> answers;

    @Getter
    @Setter
    private Integer answerNumber;

}
