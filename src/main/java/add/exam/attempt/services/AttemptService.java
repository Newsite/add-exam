package add.exam.attempt.services;

import add.exam.attempt.model.UserAnswer;
import add.exam.attempt.repositories.AttemptRepository;
import add.exam.common.services.CommonService;
import add.exam.common.services.DateService;
import add.exam.common.services.JSONMapperService;
import add.exam.exam.services.ExamService;
import add.exam.model.attempt.Attempt;
import add.exam.model.attempt.AttemptQuestion;
import add.exam.model.exam.ExamQuestion;
import add.exam.model.exam.ExamQuestionAnswer;
import com.google.common.base.Strings;
import org.dozer.DozerBeanMapper;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class AttemptService
{
    @Inject
    private AttemptRepository attemptRepository;

    @Inject
    private CommonService commonService;

    @Inject
    private ExamService examService;

    @Inject
    private JSONMapperService jsonMapper;

    @Inject
    private DozerBeanMapper dozerMapper;

    public List<AttemptQuestion> attachAttemptQuestions(Attempt attempt){
        List<ExamQuestion> questions = examService.getCompletedExamQuestions(attempt.getExam().getId());
        if (!attempt.getExam().getSettings().getRandomOrder()){
            questions = questions.subList(0, attempt.getExam().getQuestionsCount());
        }else{
            questions = commonService.getRandomSubList(questions,
                    attempt.getExam().getQuestionsCount());
        }

        List<AttemptQuestion> attemptQuestions = new ArrayList<AttemptQuestion>();
        for (ExamQuestion question: questions ){
            AttemptQuestion attemptQuestion = new AttemptQuestion();
            attemptQuestion.setAttempt(attempt);
            attemptQuestion.setQuestion(question);
            attemptQuestions.add(attemptQuestion);
        }
        return attemptQuestions;
    }

    public void create(Attempt attempt)
    {
        attempt.setStartTime(new Date());
        attemptRepository.create(attempt);
    }

    public List<UserAnswer> getUserAnswer(AttemptQuestion question)
    {
        if (!Strings.isNullOrEmpty(question.getAnswers())){
            return jsonMapper.toUserAnswerList(question.getAnswers());
        }
        List<ExamQuestionAnswer> answers = examService.getExamQuestionAnswers(question.getQuestion().getId());
        if (question.getQuestion().getRandomOrder()){
            answers = commonService.getRandomSubList(answers, answers.size());
        }
        List<UserAnswer> userAnswers = new ArrayList<UserAnswer>();
        for (ExamQuestionAnswer answer: answers){
            UserAnswer userAnswer = new UserAnswer();
            dozerMapper.map(answer, userAnswer);
            userAnswers.add(userAnswer);

        }
        question.setAnswers(jsonMapper.toJSONString(userAnswers));
        return userAnswers;
    }

    public void saveUserAnswer(List<UserAnswer> answers, Integer answerNumber, AttemptQuestion question)
    {
        if (answerNumber != null){
            for (UserAnswer answer: answers){
                answer.setSelected(false);
            }
            answers.get(answerNumber).setSelected(true);
        }
        question.setAnswers(jsonMapper.toJSONString(answers));
        commonService.update(question);
    }

    public void finish(Attempt attempt)
    {
        int correctQuestions = 0;
        for (AttemptQuestion question: attempt.getQuestions()){
            if (isCorrect(question)){
                correctQuestions++;
            }
            finishQuestion(question);
            commonService.update(question);
        }

        attempt.setResultScore(correctQuestions * 100 / attempt.getExam().getQuestionsCount());
        //set attempt totalTime
        long totalTime = DateService.getTimeRangeInSecond(attempt.getStartTime(), new Date());
        if (attempt.getExam().getTotalTime() * DateService.SECONDS_IN_MINUTE < totalTime){
            totalTime = attempt.getExam().getTotalTime() * DateService.SECONDS_IN_MINUTE;
        }
        attempt.setTotalTime(totalTime);
        attempt.setCompleted(true);
        commonService.update(attempt);
    }

    private void finishQuestion(AttemptQuestion question)
    {
        question.setBody(question.getQuestion().getBody());
        question.setExplanation(question.getQuestion().getExplanation());
        question.setType(question.getQuestion().getType());
        question.setQuestion(null);
    }

    private boolean isCorrect(AttemptQuestion question)
    {
        List<UserAnswer> answers = getUserAnswer(question);
        if(answers == null){
            return false;
        }
        boolean result = true;
        for (UserAnswer userAnswer: answers){
            ExamQuestionAnswer examQuestionAnswer = commonService.get(ExamQuestionAnswer.class, userAnswer.getId());
            userAnswer.setRight(examQuestionAnswer.isCorrect());
            if (userAnswer.getRight() != userAnswer.getSelected()){
                result = false;
            }
        }
        question.setAnswers(jsonMapper.toJSONString(answers));
        question.setCorrect(result);
        return result;
    }

    public List<Integer> getMarkedQuestions(List<AttemptQuestion> questions){
        List<Integer> result = new ArrayList<Integer>();
        int size = questions.size();
        for( int index = 0; index < size; index++){
            if (questions.get(index).isMarked()){
                result.add(index+1);
            }
        }
        return result;
    }

    public List<AttemptQuestion> getAttemptQuestions(Integer attemptId){
        return attemptRepository.getAttemptQuestions(attemptId);
    }

    public AttemptQuestion getCurrentQuestion(Integer attempt, Integer number){
        return getAttemptQuestions(attempt).get(number);
    }

    public List<Attempt> getUserAttempts(Integer userId)
    {
        return attemptRepository.getUserAttempts(userId);
    }

    public List<Attempt> getExamAttempts(Integer examId, Date dateFrom, Date dateTo)
    {
        return attemptRepository.getExamAttempts(examId, dateFrom, dateTo);
    }
}
