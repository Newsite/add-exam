package add.exam.model.exam;

import java.util.ArrayList;
import java.util.List;

public enum ExamQuestionType
{
    SINGLE_ANSWER,
    MULTI_CHOICE;

    public static List<ExamQuestionType> getQuestionTypes(){
        List<ExamQuestionType> list = new ArrayList<ExamQuestionType>();
        list.add(SINGLE_ANSWER);
        list.add(MULTI_CHOICE);
        return list;
    }
}
