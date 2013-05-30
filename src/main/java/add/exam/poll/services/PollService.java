package add.exam.poll.services;

import add.exam.common.repositories.CommonRepository;
import add.exam.exceptions.ResourceNotFoundException;
import add.exam.model.poll.Poll;
import add.exam.model.poll.PollQuestion;
import add.exam.model.poll.PollQuestionAnswer;
import add.exam.poll.repositories.PollRepository;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

@Service
public class PollService
{
    @Inject
    private CommonRepository repository;

    @Inject
    private PollRepository pollRepository;

    public Poll save(Poll poll){
        if (poll.getId() == null){
            return repository.create(poll);
        }
        return repository.update(poll);
    }

    public PollQuestion save(PollQuestion question){
        if (question.getId() == null){
            return repository.create(question);
        }
        return repository.update(question);
    }

    public PollQuestionAnswer save(PollQuestionAnswer answer){
        if (answer.getId() == null){
            return repository.create(answer);
        }
        return repository.update(answer);
    }

    public Poll getPollWithQuestions(Integer id, Integer userId)
    {
        Poll poll = pollRepository.getPollWithQuestions(id, userId);
        if (poll == null){
            throw new ResourceNotFoundException(String.format("Poll with id: %s is not found", id));
        }
        return poll;
    }

    public Poll getPoll(Integer id, Integer userId)
    {
        Poll poll = pollRepository.getPoll(id, userId);
        if (poll == null){
            throw new ResourceNotFoundException(String.format("Poll with id: %s is not found", id));
        }
        return poll;
    }

    public PollQuestion getPollQuestionWithAnswers(Integer questionId, Integer pollId, Integer userId){
        PollQuestion question = pollRepository.getPollQuestionWithAnswers(questionId, pollId, userId);
        if (question == null){
            throw new ResourceNotFoundException(String.format("Poll question with id: %s is not found", questionId));
        }
        return question;
    }

    public List<Poll> getTeacherPolls(Integer userId)
    {
        return pollRepository.getTeacherPolls(userId);
    }
}
