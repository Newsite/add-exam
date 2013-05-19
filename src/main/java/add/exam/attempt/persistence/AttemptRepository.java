package add.exam.attempt.persistence;

import add.exam.common.repository.CommonRepository;
import add.exam.model.attempt.Attempt;
import add.exam.model.attempt.AttemptQuestion;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class AttemptRepository
{
    @PersistenceContext
    private EntityManager entityManager;

    //queries
    private static final String GET_ATTEMPT_QUESTIONS_QUERY = "SELECT q from AttemptQuestion q where attempt_id = :attemptId";

    private static final String ATTEMPT_ID = "attemptId";

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
}
