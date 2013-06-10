package add.exam.poll.repositories;

import add.exam.model.poll.PollVote;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class PollVoteRepository
{
    //queries
    private static final String GET_VOTE_QUERY  = "select v from PollVote v where v.user.id = :userId and v.poll.id = :pollId";

    //attributes
    private static final String USER_ID = "userId";
    private static final String POLL_ID ="pollId";


    @PersistenceContext
    private EntityManager entityManager;

    @Transactional(readOnly = true)
    public PollVote getVote(Integer userId, Integer pollId)
    {
        List<PollVote> votes =  entityManager.createQuery(GET_VOTE_QUERY, PollVote.class)
                .setParameter(USER_ID, userId)
                .setParameter(POLL_ID, pollId)
                .getResultList();
        if (votes.isEmpty()){
            return null;
        }
        return votes.get(0);
    }
}
