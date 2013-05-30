package add.exam.poll.repositories;

import add.exam.model.poll.Poll;
import add.exam.model.poll.PollQuestion;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class PollRepository
{
    //Queries
    private static final String GET_POLL_WITH_QUESTIONS_QUERY = "select p from Poll p left join fetch p.questions " +
            "where p.id = :pollId and p.user.id = :userId";
    private static final String GET_POLL_QUERY = "select p from Poll p where id = :pollId and p.user.id = :userId";
    private static final String GET_POLL_QUESTION_WITH_ANSWERS_QUERY = "select q from PollQuestion q left join fetch q.answers " +
            "where q.id = :pollQuestionId and q.poll.id = :pollId and q.poll.user.id = :userId";
    private static final String GET_TEACHER_POLLS_QUERY = "select p from Poll p where p.user.id = :userId";

    //attributes
    private static final String USER_ID = "userId";
    private static final String POLL_ID = "pollId";
    private static final String POLL_QUESTION_ID = "pollQuestionId";

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional(readOnly = true)
    public Poll getPollWithQuestions(Integer id, Integer userId)
    {
        return getPoll(id, userId, GET_POLL_WITH_QUESTIONS_QUERY);
    }

    @Transactional(readOnly = true)
    public Poll getPoll(Integer id, Integer userId)
    {
        return getPoll(id, userId, GET_POLL_QUERY);
    }

    @Transactional(readOnly = true)
    private Poll getPoll(Integer id, Integer userId, String query){
        List<Poll> polls = entityManager.createQuery(query, Poll.class)
                .setParameter(POLL_ID, id)
                .setParameter(USER_ID, userId)
                .getResultList();
        if (polls.isEmpty()){
            return null;
        }
        return polls.get(0);
    }

    public PollQuestion getPollQuestionWithAnswers(Integer questionId, Integer pollId, Integer userId)
    {
        List<PollQuestion> questions = entityManager.createQuery(GET_POLL_QUESTION_WITH_ANSWERS_QUERY, PollQuestion.class)
                .setParameter(POLL_ID, pollId)
                .setParameter(USER_ID, userId)
                .setParameter(POLL_QUESTION_ID, questionId)
                .getResultList();
        if (questions.isEmpty()){
            return null;
        }
        return questions.get(0);
    }

    public List<Poll> getTeacherPolls(Integer userId)
    {
        return entityManager.createQuery(GET_TEACHER_POLLS_QUERY, Poll.class)
                .setParameter(USER_ID, userId)
                .getResultList();
    }
}
