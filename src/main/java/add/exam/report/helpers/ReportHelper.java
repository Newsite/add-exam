package add.exam.report.helpers;

import add.exam.common.services.DateService;
import add.exam.model.attempt.Attempt;
import add.exam.model.exam.Exam;
import add.exam.report.model.ExamInfo;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * add attributes to model for report pages
 */
@Component
public class ReportHelper
{
    //attributes
    private static final String EXAMS_INFO = "examsInfo";
    private static final String ATTEMPTS = "attempts";
    private static final String AVERAGE_TIME = "averageTime";
    private static final String AVERAGE_SCORE = "averageScore";
    private static final String BEST_SCORE = "bestScore";
    private static final String BEST_STUDENT = "bestStudent";
    private static final String EXAM = "exam";

    /**
     * add attributes to teacher report page
     * @param exams
     *          list of exams
     * @param model
     *          page model
     * @param dateFrom
     *          from date
     * @param dateTo
     *          to date
     */
    public void addTeacherReportAttr(List<Exam> exams, Model model, Date dateFrom, Date dateTo)
    {
        List<ExamInfo> examsInfo = new ArrayList<ExamInfo>();
        for (Exam exam: exams){
            ExamInfo info = new ExamInfo();
            info.setExam(exam);
            if (exam.getAttempts() != null){
                Integer averageScore = 0;
                Long averageTime = 0L;
                Integer attemptCount = 0;
                Integer successCount = 0;
                for (Attempt attempt: exam.getAttempts()){
                    if (isIncluded(attempt, dateFrom, dateTo)){
                        averageScore += attempt.getResultScore();
                        attemptCount ++;
                        averageTime += attempt.getTotalTime();
                        if (attempt.getResultScore() >= exam.getPassScore()){
                            successCount ++;
                        }
                    }
                }
                if (attemptCount > 0){
                    info.setAverageScore(averageScore / attemptCount);
                    averageTime = averageTime / attemptCount;
                    String time = DateService.getTimeString(averageTime);
                    info.setAverageTime(time);
                    info.setNumberOfAttempts(attemptCount);
                    info.setNumberOfSuccessAttempts(successCount);
                }
            }
            examsInfo.add(info);
        }
        model.addAttribute(EXAMS_INFO, examsInfo);
    }

    //checks if attempt should be included in report
    private boolean isIncluded(Attempt attempt, Date dateFrom, Date dateTo)
    {
        return attempt.getCompleted() && DateService.isDateInRange(attempt.getStartTime(), dateFrom, dateTo);
    }

    public void addStudentReportAttr(List<Attempt> attempts, Model model, Date fromDate, Date toDate)
    {
        List<Attempt> resultList = new ArrayList<Attempt>();
        Integer score = 0;
        Long time = 0L;

        if (attempts != null){
            for(Attempt attempt: attempts){
                if (DateService.isDateInRange(attempt.getStartTime(), fromDate, toDate)){
                    resultList.add(attempt);
                    score += attempt.getResultScore();
                    time += attempt.getTotalTime();
                }
            }
        }
        if (!resultList.isEmpty()){
            model.addAttribute(AVERAGE_SCORE, score / resultList.size());
            model.addAttribute(AVERAGE_TIME, DateService.getTimeString(time / resultList.size()));
        }
        model.addAttribute(ATTEMPTS, resultList);
    }

    public void addExamReportAttr(Exam exam, Model model)
    {
        model.addAttribute(ATTEMPTS, exam.getAttempts());
        Integer score = 0;
        Long time = 0L;
        Integer bestScore = 0;
        String bestStudent = "";
        for (Attempt attempt: exam.getAttempts()){
            score += attempt.getResultScore();
            time += attempt.getTotalTime();
            if (attempt.getResultScore() > bestScore){
                bestScore = attempt.getResultScore();
                bestStudent = String.format("%s %s", attempt.getUser().getFirstName(), attempt.getUser().getLastName());
            }
        }
        if (! exam.getAttempts().isEmpty()){
            model.addAttribute(AVERAGE_SCORE, score / exam.getAttempts().size());
            model.addAttribute(AVERAGE_TIME, DateService.getTimeString(time / exam.getAttempts().size()));
            model.addAttribute(BEST_SCORE, bestScore);
            model.addAttribute(BEST_STUDENT, bestStudent);
        }
        model.addAttribute(EXAM, exam);
    }
}
