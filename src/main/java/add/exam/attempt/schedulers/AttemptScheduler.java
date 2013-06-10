package add.exam.attempt.schedulers;

import add.exam.attempt.services.AttemptService;
import add.exam.common.services.CommonService;
import add.exam.common.services.DateService;
import add.exam.model.attempt.Attempt;
import add.exam.model.attempt.AttemptQuestion;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class AttemptScheduler
{
    private static final Integer MILLISECONDS_IN_WEEK = 604800000;

    @Inject
    private AttemptService attemptService;

    @Inject
    private CommonService commonService;

    @Scheduled (cron="0 0 0/1 * * ?")
    public void removeTimeOutedAttempts(){
        List<Attempt> attempts = attemptService.getNotCompletedAttempts();
        for (Attempt attempt: attempts){
            if (DateService.getTimeLeftInSeconds(new Date(), attempt.getStartTime(), attempt.getExam().getTotalTime()) < 0){
                attemptService.finish(attempt);
            }
        }
    }

    @Scheduled(cron="0 0 0 * * 1")
    public void removeAttemptQuestions(){
        Date currentDate = new Date();
        Date weekAgoDate = new Date(currentDate.getTime() - MILLISECONDS_IN_WEEK);
        Date twoWeekAgoDate = new Date(currentDate.getTime() - 2 * MILLISECONDS_IN_WEEK);
        List<Attempt> attempts = attemptService.getAttemptsCompletedInRange(twoWeekAgoDate, weekAgoDate);
        for (Attempt attempt: attempts){
            attempt.setQuestions(new ArrayList<AttemptQuestion>());
            commonService.update(attempt);
        }

    }
}
