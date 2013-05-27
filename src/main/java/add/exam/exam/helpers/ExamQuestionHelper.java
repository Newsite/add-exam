package add.exam.exam.helpers;

import add.exam.model.exam.ExamQuestion;
import add.exam.model.exam.ExamQuestionAnswer;
import add.exam.model.exam.ExamQuestionType;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import java.util.List;

import static add.exam.model.exam.ExamQuestionType.MULTI_CHOICE;
import static add.exam.model.exam.ExamQuestionType.SINGLE_ANSWER;

@Component
public class ExamQuestionHelper
{
    //model attributes
    private static final String QUESTION = "question";
    private static final String TYPES = "types";
    private static final String NOT_COMPLETED_MESSAGE = "notCompletedMsg";
    private static final String ANSWERS = "answers";
    private static final String ANSWER = "answer";
    private static final String SHOW_EXPLANATION = "showExplanationTextarea";
    private static final String SHOW_ADD_ANSWER_BTN = "showAddAnswerButton";

    //error message
    private static final String NOT_ENOUGH_QUESTION_ERROR_MSG = "Your question should contain at least two answers!";
    private static final String NO_CORRECT_ANSWERS_ERROR_MSG = "At least one answer should be correct!";
    private static final String NO_SINGLE_ANSWER_ERROR_MSG = "Single answer question should contain only one correct answer!";

    public void editQuestion(ExamQuestion question, List<ExamQuestionAnswer> answers, Model model){
        addQuestionToModel(question, model);
        model.addAttribute(ANSWERS, answers);
        // hide add answer if answers more than 7
        if (answers.size() < 8 ){
            model.addAttribute(SHOW_ADD_ANSWER_BTN, true);
        }
        addQuestionStatusAttributes(question, answers, model);
    }

    public void addQuestionStatusAttributes(ExamQuestion question, List<ExamQuestionAnswer> answers, Model model){
        if (!question.getCompleted()){
            String msg = getUncompletedQuestionErrorMsg(answers, question.getType());
            model.addAttribute(NOT_COMPLETED_MESSAGE, msg);
        }
    }

    public void addQuestionToModel(ExamQuestion question, Model model){
        model.addAttribute(ANSWER, new ExamQuestionAnswer());
        model.addAttribute(QUESTION, question);
        model.addAttribute(TYPES, ExamQuestionType.getQuestionTypes());
        if (question.getExam().getSettings().getShowAnswers()){
            model.addAttribute(SHOW_EXPLANATION, true);
        }
    }

    private String getUncompletedQuestionErrorMsg(List<ExamQuestionAnswer> answers, ExamQuestionType type){
        if (answers.size() < 2){
            return NOT_ENOUGH_QUESTION_ERROR_MSG;
        }
        Integer correctAnswers = getCorrectAnswersNumber(answers);
        if (type == SINGLE_ANSWER && correctAnswers > 1 ){
            return NO_SINGLE_ANSWER_ERROR_MSG;
        }
        return NO_CORRECT_ANSWERS_ERROR_MSG;
    }

    public Boolean isQuestionCompleted(List<ExamQuestionAnswer> answers, ExamQuestionType type)
    {
        if (answers == null || answers.isEmpty() || answers.size() < 2 ){
            return false;
        }
        Integer correctAnswers = getCorrectAnswersNumber(answers);
        return !((type.equals(SINGLE_ANSWER) && correctAnswers != 1) ||
                (type.equals(MULTI_CHOICE) && correctAnswers == 0));
    }

    private Integer getCorrectAnswersNumber(List<ExamQuestionAnswer> answers){
        Integer result = 0;
        for(ExamQuestionAnswer answer: answers){
            if (answer.isCorrect()){
                result ++;
            }
        }
        return result;
    }


}
