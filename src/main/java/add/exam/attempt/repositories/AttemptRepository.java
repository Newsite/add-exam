package add.exam.attempt.repositories;

import add.exam.common.repositories.CommonRepository;
import add.exam.common.services.DateService;
import add.exam.model.attempt.Attempt;
import add.exam.model.attempt.AttemptQuestion;
import add.exam.model.exam.Exam;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Date;
import java.util.List;

@Repository
public class AttemptRepository
{
    @PersistenceContext
    private EntityManager entityManager;

    //queries
    private static final String GET_ATTEMPT_QUESTIONS_QUERY = "SELECT q from AttemptQuestion q where attempt_id = :attemptId";
    private static final String GET_USER_ATTEMPTS_QUERY = "Select a from Attempt a where user_id = :userId";
    private static final String GET_EXAM_WITH_ATTEMPTS_QUERY = "SELECT distinct(a) from Attempt a where a.exam.id = :examId and a.completed = 'true' and a.startTime between :start and :end";

    //params
    private static final String ATTEMPT_ID = "attemptId";
    private static final String USER_ID = "userId";
    private static final String EXAM_ID = "examId";
    private static final String START_DATE = "start";
    private static final String END_DATE = "end";

    @Transactional(readOnly = true)
    public List<AttemptQuestion> getAttemptQuestions(Integer attemptId){
        return entityManager.createQuery(String.format(CommonRepository.TWO_STRING_PATTERN,
                GET_ATTEMPT_QUESTIONS_QUERY, CommonRepository.ORDER_BY_ID), AttemptQuestion.class)
                .setParameter(ATTEMPT_ID, attemptId)
                .getResultList();
    }

    @Transactional
    public void create(Attempt attempt){
        entityManager.persist(attempt);
        for (AttemptQuestion question: attempt.getQuestions()){
            entityManager.persist(question);
        }
    }

    /**
     * returns user attempts list
     * @param userId
     *          user id
     * @return
     *      user attempts list
     */
    @Transactional(readOnly = true)
    public List<Attempt> getUserAttempts(Integer userId)
    {
        return entityManager.createQuery(GET_USER_ATTEMPTS_QUERY, Attempt.class)
                .setParameter(USER_ID, userId)
                .getResultList();
    }


    public List<Attempt> getExamAttempts(Integer examId, Date dateFrom, Date dateTo)
    {
        dateFrom = DateService.getDateFrom(dateFrom);
        dateTo = DateService.getDateTo(dateTo);

        return  entityManager.createQuery( GET_EXAM_WITH_ATTEMPTS_QUERY, Attempt.class)
                .setParameter(EXAM_ID, examId)
                .setParameter(START_DATE, dateFrom)
                .setParameter(END_DATE, dateTo)
                .getResultList();
    }
}
