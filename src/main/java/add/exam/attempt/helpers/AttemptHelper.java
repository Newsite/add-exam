package add.exam.attempt.helpers;

import add.exam.attempt.forms.AttemptQuestionForm;
import add.exam.attempt.model.UserAnswer;
import add.exam.common.services.DateService;
import add.exam.model.attempt.Attempt;
import add.exam.model.attempt.AttemptQuestion;
import add.exam.model.exam.ExamQuestionType;
import com.google.common.base.Strings;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import java.util.Date;
import java.util.List;

@Component
public class AttemptHelper
{
    //model attributes
    private static final String RESULT = "result";
    private static final String FAILED = "failed";
    private static final String PASSED = "passed";
    private static final String MARKED_URL = "markedUrl";
    private static final String FIRST_QUESTION = "firstQuestion";
    private static final String LAST_QUESTION = "lastQuestion";
    private static final String CURRENT_QUESTION = "currentQuestion";
    private static final String IS_NOT_ANSWERED = "isNotAnswered";
    private static final String QUESTIONS = "questions";
    private static final String ATTEMPT_ID = "attemptId";
    private static final String NO_MARKED_QUESTIONS = "noMarkedQuestions";
    private static final String EXAM_NAME = "examName";
    private static final String FORM = "form";
    private static final String ATTEMPT = "attempt";
    private static final String TIME = "time";
    private static final String SHOW_IMAGE = "showImage";

    public void showAttemptResult(Attempt attempt, Model model){
        model.addAttribute(RESULT, attempt);
        if (attempt.getResultScore() < attempt.getExam().getPassScore()){
            model.addAttribute(FAILED, true);
        }else{
            model.addAttribute(PASSED, true);
        }
        model.addAttribute(TIME, DateService.getTimeString(attempt.getTotalTime()));
    }

    public void viewQuestion(AttemptQuestionForm form, Attempt attempt, AttemptQuestion question, Model model){
        if (attempt.getCurrentQuestionNumber() == 0){
            model.addAttribute(FIRST_QUESTION, true);
        }
        if (attempt.getCurrentQuestionNumber() + 1  == attempt.getExam().getQuestionsCount()){
            model.addAttribute(LAST_QUESTION, true);
        }

        if(!isQuestionAnswered(form.getAnswers())){
            model.addAttribute(IS_NOT_ANSWERED, true);
        }
        if (question.getQuestion().getType() == ExamQuestionType.SINGLE_ANSWER){
            setAnswerNumber(form);
        }
        model.addAttribute(FORM, form);
        model.addAttribute(ATTEMPT, attempt);
        model.addAttribute(ATTEMPT_ID, attempt.getId());
        //attribute that is responsible for question type
        model.addAttribute(question.getQuestion().getType().toString(), true);

        addTimeAttribute(attempt, model);
        if (!Strings.isNullOrEmpty(question.getQuestion().getImage())){
            model.addAttribute(SHOW_IMAGE, true);
        }
        model.addAttribute(MARKED_URL, String.format("mark/%s.html", question.getId()));
    }

    private void setAnswerNumber(AttemptQuestionForm form)
    {
        for (UserAnswer answer: form.getAnswers()){
            if (answer.getSelected()){
                form.setAnswerNumber(form.getAnswers().indexOf(answer));
                return;
            }
        }
    }

    public void addMarkedListAttributes(Model model, List<Integer> questions, Attempt attempt){
        model.addAttribute(QUESTIONS, questions);
        model.addAttribute(EXAM_NAME, attempt.getExam().getName());
        if (questions.isEmpty()){
            model.addAttribute(NO_MARKED_QUESTIONS, true);
        }
        model.addAttribute(ATTEMPT_ID, attempt.getId());
        model.addAttribute(CURRENT_QUESTION, attempt.getCurrentQuestionNumber() + 1);
        addTimeAttribute(attempt, model);
    }

    private boolean isQuestionAnswered(List<UserAnswer> answers){
        for (UserAnswer answer: answers){
            if (answer.getSelected()){
                return true;
            }
        }
        return false;
    }

    private void addTimeAttribute(Attempt attempt, Model model){
        if ( attempt.getExam().getTotalTime() != null ){
            Long time = DateService.getTimeLeftInSeconds(new Date(), attempt.getStartTime(), attempt.getExam().getTotalTime());
            model.addAttribute(TIME, time);
        }
    }
}
