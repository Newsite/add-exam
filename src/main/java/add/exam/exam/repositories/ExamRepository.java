package add.exam.exam.repositories;

import add.exam.common.repository.CommonRepository;
import add.exam.model.exam.Exam;
import add.exam.model.exam.ExamQuestion;
import add.exam.model.exam.ExamQuestionAnswer;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class ExamRepository
{
    private static final String LIKE_PATTERN = "%%s%";

    //Queries
    private static final String GET_TEACHER_EXAMS_QUERY = "SELECT e from Exam e where user_id = :userId";
    private static final String GET_TEACHER_EXAMS_BY_NAME_QUERY = "SELECT e from Exam e where user_id = :userId and name LIKE :name ";
    private static final String GET_COMPLETED_QUESTIONS_NUMBER_QUERY =
            "SELECT count(q) from ExamQuestion q where q.exam_id = :examId and q.completed = true order by id";
    private static final String GET_EXAM_QUESTIONS_QUERY = "SELECT q from ExamQuestion q where exam_id = :examId";
    private static final String COMPLETED_CONDITION = " and completed = :completed";
    private static final String GET_QUESTION_ANSWERS_QUERY = "SELECT a from ExamQuestionAnswer a where question_id = :questionId";
    private static final String GET_ACTIVE_ATTEMPTS_NUMBER = "SELECT count(a) from Attempt a where exam_id = :examId and completed = :completed";
    private static final String GET_USER_EXAM_QUERY = "SELECT e from Exam e where id = :examId and user_id = :userId";

    //parameters
    private static final String EXAM_ID = "examId";
    private static final String QUESTION_ID = "questionId";
    private static final String COMPLETED = "completed";
    private static final String USER_ID = "userId";
    private static final String NAME = "name";

    @PersistenceContext
    private EntityManager entityManager;

   @Transactional(readOnly = true)
   public List<Exam> getTeacherExams(Integer userId){
       return entityManager.createQuery(String.format(CommonRepository.TWO_STRING_PATTERN, GET_TEACHER_EXAMS_QUERY, CommonRepository.ORDER_BY_ID),Exam.class)
               .setParameter(USER_ID, userId)
               .getResultList();
   }

   @Transactional(readOnly = true)
   public Long getCompletedQuestionsNumber(Integer examId){
       return entityManager.createQuery(GET_COMPLETED_QUESTIONS_NUMBER_QUERY, Long.class)
               .setParameter(EXAM_ID, examId)
               .getSingleResult();
   }

   @Transactional(readOnly = true)
   public List<ExamQuestion> getExamQuestions(Integer examId){
       return entityManager.createQuery(String.format(CommonRepository.TWO_STRING_PATTERN,
               GET_EXAM_QUESTIONS_QUERY, CommonRepository.ORDER_BY_ID), ExamQuestion.class)
               .setParameter(EXAM_ID, examId)
               .getResultList();
   }

   @Transactional(readOnly = true)
   public List<ExamQuestionAnswer> getQuestionAnswers(Integer questionId){
        return entityManager.createQuery(String.format(CommonRepository.TWO_STRING_PATTERN,
                GET_QUESTION_ANSWERS_QUERY, CommonRepository.ORDER_BY_ID), ExamQuestionAnswer.class)
                .setParameter(QUESTION_ID, questionId)
                .getResultList();
   }

    @Transactional(readOnly = true)
    public List<ExamQuestion> getCompletedExamQuestions(Integer examId)
    {
        return entityManager.createQuery(String.format(CommonRepository.THREE_STRING_PATTERN,
                GET_EXAM_QUESTIONS_QUERY, COMPLETED_CONDITION, CommonRepository.ORDER_BY_ID),  ExamQuestion.class)
                .setParameter(EXAM_ID, examId)
                .setParameter(COMPLETED, true)
                .getResultList();
    }

    @Transactional(readOnly = true)
    public Long getActiveAttemptsNumber(Integer examId)
    {
        return entityManager.createQuery(GET_ACTIVE_ATTEMPTS_NUMBER, Long.class)
                .setParameter(EXAM_ID, examId)
                .setParameter(COMPLETED, false)
                .getSingleResult();
    }

    public Exam getExam(Integer id, Integer userId)
    {
        List<Exam> exams = entityManager.createQuery(GET_USER_EXAM_QUERY, Exam.class)
               .setParameter(EXAM_ID, id)
               .setParameter(USER_ID, userId)
               .getResultList();
        if (exams.isEmpty()){
           return null;
        }
        return exams.get(0);
    }

    public List<Exam> getTeacherExams(String pattern, Integer userId)
    {
        return entityManager.createQuery( GET_TEACHER_EXAMS_BY_NAME_QUERY, Exam.class)
                .setParameter(USER_ID, userId)
                .setParameter(NAME, "%" + pattern + "%")
                .getResultList();
    }
}
