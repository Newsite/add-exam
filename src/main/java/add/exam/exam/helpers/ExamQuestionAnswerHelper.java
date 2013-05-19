package add.exam.exam.helpers;

import add.exam.model.exam.ExamQuestionAnswer;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;

@Component
public class ExamQuestionAnswerHelper
{
    //model attributes
    private static final String ANSWER = "answer";

    public void addAnswerToModel(ExamQuestionAnswer answer, Model model){
        model.addAttribute(ANSWER, answer);
    }

    public List<ExamQuestionAnswer> replaceAnswer(List<ExamQuestionAnswer> answers, ExamQuestionAnswer answer)
    {
        if (answers == null){
            return null;
        }
        List<ExamQuestionAnswer> result = new ArrayList<ExamQuestionAnswer>();
        for(ExamQuestionAnswer item: answers){
            if (item.getId().equals(answer.getId())){
                result.add(answer);
            }else{
                result.add(item);
            }
        }
        return result;
    }
}
