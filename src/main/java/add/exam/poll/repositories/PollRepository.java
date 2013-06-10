package add.exam.poll.repositories;

import add.exam.model.poll.Poll;
import add.exam.model.poll.PollQuestion;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigInteger;
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
    private static final String GET_TEACHER_POLLS_BY_NAME_QUERY = "SELECT p from Poll p where user_id = :userId and name LIKE :name ";

    private static final String IS_ACCESS_ALLOWED_TO_POLL = "select count(p.*) from polls_in_groups p " +
            "left join students_in_groups s on s.group_id = p.group_id " +
            "where p.poll_id = :pollId and s.student_id = :userId";

    private static final String GET_POLL_BY_ID_QUERY = "select p from Poll p left join fetch p.questions where p.id = :pollId";
    private static final String GET_POLL_QUESTION_BY_ID_WITH_ANSWERS_QUERY = "select q from PollQuestion q left join fetch q.answers where q.id = :pollQuestionId";
    private static final String GET_POLL_QUESTIONS_AND_ANSWERS_BY_ID_QUERY = "select distinct(q) from PollQuestion q left join fetch q.answers where q.poll.id = :pollId";
    private static final String GET_STUDENT_POLLS_NATIVE_QUERY = "select distinct(p.*) from polls p " +
            "left join polls_in_groups pig on pig.poll_id = p.id " +
            "left join students_in_groups sig on sig.group_id = pig.group_id where student_id = :studentId";

    //attributes
    private static final String USER_ID = "userId";
    private static final String POLL_ID = "pollId";
    private static final String POLL_QUESTION_ID = "pollQuestionId";
    private static final String NAME = "name";
    private static final String STUDENT_ID = "studentId";

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

    @Transactional(readOnly = true)
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

    @Transactional(readOnly = true)
    public List<Poll> getTeacherPolls(Integer userId)
    {
        return entityManager.createQuery(GET_TEACHER_POLLS_QUERY, Poll.class)
                .setParameter(USER_ID, userId)
                .getResultList();
    }

    @Transactional(readOnly = true)
    public List<Poll> getTeacherPolls(String pattern, Integer userId)
    {
        return entityManager.createQuery( GET_TEACHER_POLLS_BY_NAME_QUERY, Poll.class)
                .setParameter(USER_ID, userId)
                .setParameter(NAME, "%" + pattern + "%")
                .getResultList();
    }

    @Transactional(readOnly = true)
    public boolean isAccessAllowed(Integer userId, Integer pollId)
    {
        BigInteger count = (BigInteger) entityManager.createNativeQuery(IS_ACCESS_ALLOWED_TO_POLL)
                .setParameter(USER_ID, userId)
                .setParameter(POLL_ID, pollId)
                .getSingleResult();
         return count.intValue() > 0;
    }

    @Transactional(readOnly = true)
    public Poll getPollWithQuestions(Integer id)
    {
        List<Poll> polls = entityManager.createQuery(GET_POLL_BY_ID_QUERY, Poll.class)
                .setParameter(POLL_ID, id)
                .getResultList();
        if (polls.isEmpty()){
            return null;
        }
        return polls.get(0);
    }

    @Transactional(readOnly = true)
    public PollQuestion getPollQuestionWithAnswers(Integer questionId)
    {
        List<PollQuestion> questions = entityManager.createQuery(GET_POLL_QUESTION_BY_ID_WITH_ANSWERS_QUERY,
                PollQuestion.class)
                .setParameter(POLL_QUESTION_ID, questionId)
                .getResultList();
        if (questions.isEmpty()){
            return null;
        }
        return questions.get(0);
    }

    public List<PollQuestion> getPollQuestionsAndAnswers(Integer pollId)
    {
        return entityManager.createQuery(GET_POLL_QUESTIONS_AND_ANSWERS_BY_ID_QUERY, PollQuestion.class)
                .setParameter(POLL_ID, pollId)
                .getResultList();
    }

    public List<Poll> getStudentPolls(Integer studentId)
    {
        return entityManager.createNativeQuery(GET_STUDENT_POLLS_NATIVE_QUERY, Poll.class)
                .setParameter(STUDENT_ID, studentId)
                .getResultList();
    }
}
