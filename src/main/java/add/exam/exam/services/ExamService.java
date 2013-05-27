package add.exam.exam.services;

import add.exam.common.repositories.CommonRepository;
import add.exam.exam.repositories.ExamRepository;
import add.exam.exceptions.ResourceNotFoundException;
import add.exam.model.exam.Exam;
import add.exam.model.exam.ExamQuestion;
import add.exam.model.exam.ExamQuestionAnswer;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.Date;
import java.util.List;

@Service
public class ExamService
{
    @Inject
    private CommonRepository repository;

    @Inject
    private ExamRepository examRepository;

    public Exam save(Exam exam){
        if (exam.getId() == null){
            return repository.create(exam);
        }
        return repository.update(exam);
    }

    public ExamQuestion save(ExamQuestion question){
        if (question.getId() == null){
            return repository.create(question);
        }
        return repository.update(question);
    }

    public ExamQuestionAnswer save(ExamQuestionAnswer answer){
        if (answer.getId() == null){
            return repository.create(answer);
        }
        return repository.update(answer);
    }

    public List<Exam> getTeacherExams(Integer userId){
        return examRepository.getTeacherExams(userId);
    }

    public List<ExamQuestion> getExamQuestions(Integer examId){
        return examRepository.getExamQuestions(examId);
    }

    public List<ExamQuestion> getCompletedExamQuestions(Integer examId){
        return examRepository.getCompletedExamQuestions(examId);
    }

    public List<ExamQuestionAnswer> getExamQuestionAnswers(Integer questionId){
        return examRepository.getQuestionAnswers(questionId);
    }

    public boolean isExamCompleted(Exam exam, List<ExamQuestion> questions){
        if (questions == null){
            questions = getExamQuestions(exam.getId());
        }
        return exam.getQuestionsCount() <= getCompletedQuestionsCount(questions);
    }

    private Integer getCompletedQuestionsCount(List<ExamQuestion> questions){
        Integer result = 0;
        if (questions == null){
            return result;
        }
        for (ExamQuestion question: questions){
            if (question.getCompleted()){
                result ++;
            }
        }
        return result;
    }

    public void setExamPublished(Exam exam){
        if (exam.getPublished()){
            boolean isCompletedExam = isExamCompleted(exam, null);
            if (!isCompletedExam){
                exam.setPublished(false);
                save(exam);
            }
        }
    }

    public Long getActiveAttemptsNumber(Integer examId){
        return examRepository.getActiveAttemptsNumber(examId);
    }

    public boolean isExamEditingClosed(Exam exam)
    {
        if (exam.getPublished()){
            return true;
        }
        Long activeAttemptsNumber = getActiveAttemptsNumber(exam.getId());
        return activeAttemptsNumber > 0;
    }

    public Exam getExam(Integer id, Integer userId){
        Exam exam = examRepository.getExam(id, userId);
        if ( exam == null ){
            throw new ResourceNotFoundException(String.format("Exam with id = %s user_id = %s not found!", id, userId));
        }
        return exam;
    }

    public List<Exam> getExams(String pattern, Integer userId)
    {
        return examRepository.getTeacherExams(pattern, userId);
    }

    public List<Exam> getTeacherExamsWithAttempts(Integer userId)
    {
        return examRepository.getTeacherExamsWithAttempts(userId);
    }

    public List<Exam> getStudentExams(Integer studentId)
    {
        return examRepository.getStudentExams(studentId);
    }
}
