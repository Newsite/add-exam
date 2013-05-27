package add.exam.report.controllers;

import add.exam.attempt.services.AttemptService;
import add.exam.exam.services.ExamService;
import add.exam.model.attempt.Attempt;
import add.exam.model.exam.Exam;
import add.exam.model.user.User;
import add.exam.report.helpers.ReportHelper;
import add.exam.user.services.LoginService;
import com.google.common.base.Strings;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.inject.Inject;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping(ReportController.REQUEST_URL)
public class ReportController
{
    public static final String REQUEST_URL = "/reports";

    private final DateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy");

    //thymeleaf templates
    private static final String STUDENT_REPORT_TEMPLATE = "reports/studentReport";
    private static final String ADMIN_REPORT_TEMPLATE = "reports/adminReport";
    private static final String TEACHER_REPORT_TEMPLATE = "reports/teacherReport";
    private static final String TEACHER_EXAM_REPORT_TEMPLATE = "reports/teacherExamReport";
    private static final String TEACHER_REPORT_WITH_DATE_PARAMS_TEMPLATE = "common/ajax-templates/teacherReport";
    private static final String STUDENT_REPORT_WITH_DATE_PARAMS_TEMPLATE = "common/ajax-templates/studentReport";
    private static final String TEACHER_EXAM_REPORT_WITH_DATE_PARAMS_TEMPLATE = "common/ajax-templates/teacherExamReport";

    @Inject
    private ExamService examService;

    @Inject
    private LoginService userService;

    @Inject
    private ReportHelper reportHelper;

    @Inject
    private AttemptService attemptService;

    /**
     * Shows proper report according to user role
     * @param model
     *          page model
     * @return
     *      report page
     */
    @RequestMapping(method = RequestMethod.GET)
    public String showReport(Model model){
        User user = userService.getUser();
        switch(user.getRole()){
            case ROLE_TEACHER: return showTeacherReport(model);
            case ROLE_ADMIN: return showAdminReport(model);
            default: return  showStudentReport(model);
        }
    }

    /**
     * Shows teacher exams report page
     * @param model
     *          page model
     * @return
     *      teacher exams report page
     */
    private String showTeacherReport(Model model)
    {
        User user = userService.getUser();
        List<Exam> exams = examService.getTeacherExamsWithAttempts(user.getId());
        reportHelper.addTeacherReportAttr(exams, model, null, null);
        return TEACHER_REPORT_TEMPLATE;
    }

    private String showAdminReport(Model model)
    {
        return ADMIN_REPORT_TEMPLATE;
    }

    /**
     * Shows student attempts report page
     * @param model
     *          page model
     * @return
     *      report page
     */
    private String showStudentReport(Model model)
    {
        User user = userService.getUser();
        List<Attempt> attempts = attemptService.getUserAttempts(user.getId());
        reportHelper.addStudentReportAttr(attempts, model, null, null);
        return STUDENT_REPORT_TEMPLATE;
    }

    /**
     * Is used for teacher exams report ajax search
     * @param dateFrom
     *          from date
     * @param dateTo
     *          to date
     * @param model
     *          page model
     * @return
     *      list exams report in date range
     * @throws ParseException
     */
    @RequestMapping(value = "/teacher", method = RequestMethod.POST)
    public String getTeacherReport(@RequestParam String dateFrom, @RequestParam String dateTo, Model model)
            throws ParseException
    {
        User user = userService.getUser();
        List<Exam> exams = examService.getTeacherExamsWithAttempts(user.getId());
        Date fromDate = Strings.isNullOrEmpty(dateFrom) ? null : dateFormatter.parse(dateFrom);
        Date toDate = Strings.isNullOrEmpty(dateTo) ? null : dateFormatter.parse(dateTo);
        reportHelper.addTeacherReportAttr(exams, model, fromDate, toDate);
        return TEACHER_REPORT_WITH_DATE_PARAMS_TEMPLATE;
    }

    /**
     * Is used for students attempts ajax search
     * @param dateFrom
     *          from date
     * @param dateTo
     *          to date
     * @param model
     *          page model
     * @return
     *      list attempts report in date range
     * @throws ParseException
     */
    @RequestMapping(value = "/student", method = RequestMethod.POST)
    public String getStudentReport(@RequestParam String dateFrom, @RequestParam String dateTo, Model model)
            throws ParseException
    {
        User user = userService.getUser();
        List<Attempt> attempts = attemptService.getUserAttempts(user.getId());
        Date fromDate = Strings.isNullOrEmpty(dateFrom) ? null : dateFormatter.parse(dateFrom);
        Date toDate = Strings.isNullOrEmpty(dateTo) ? null : dateFormatter.parse(dateTo);
        reportHelper.addStudentReportAttr(attempts, model, fromDate, toDate);
        return STUDENT_REPORT_WITH_DATE_PARAMS_TEMPLATE;
    }

    @RequestMapping(value = "/exam/{examId}", method = RequestMethod.GET)
    public String getTeacherExamReport(@PathVariable Integer examId, Model model){
        addTeacherExamAttr(examId, model, null, null);
        return TEACHER_EXAM_REPORT_TEMPLATE;
    }

    @RequestMapping(value = "/exam/{examId}", method = RequestMethod.POST)
    public String getTeacherExamReportInDateRange(@PathVariable Integer examId, Model model,
                                                  @RequestParam String dateFrom, @RequestParam String dateTo)
            throws ParseException
    {
        Date fromDate = Strings.isNullOrEmpty(dateFrom) ? null : dateFormatter.parse(dateFrom);
        Date toDate = Strings.isNullOrEmpty(dateTo) ? null : dateFormatter.parse(dateTo);
        addTeacherExamAttr(examId, model, fromDate, toDate);
        return TEACHER_EXAM_REPORT_WITH_DATE_PARAMS_TEMPLATE;
    }

    private void addTeacherExamAttr(Integer examId, Model model, Date dateFrom, Date dateTo )
    {
        User user = userService.getUser();
        Exam exam = examService.getExam(examId, user.getId());
        List<Attempt> attempts = attemptService.getExamAttempts(examId, dateFrom, dateTo);
        exam.setAttempts(attempts);
        reportHelper.addExamReportAttr(exam, model);
    }

}
