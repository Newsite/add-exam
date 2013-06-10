package add.exam.poll.services;

import add.exam.common.repositories.CommonRepository;
import add.exam.common.services.CommonService;
import add.exam.model.poll.PollVote;
import add.exam.poll.repositories.PollVoteRepository;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

@Service
public class PollVoteService
{

    @Inject
    private PollVoteRepository repository;

    @Inject
    private CommonRepository commonRepository;

    public PollVote getVote(Integer userId, Integer pollId)
    {
        return repository.getVote(userId, pollId);
    }

    public PollVote save(PollVote vote)
    {
        if (vote.getId() == null){
            return commonRepository.create(vote);
        }
        return commonRepository.update(vote);
    }
}
