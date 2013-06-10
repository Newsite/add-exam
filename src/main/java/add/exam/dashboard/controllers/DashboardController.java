package add.exam.dashboard.controllers;

import add.exam.dashboard.helpers.DashboardHelper;
import add.exam.exam.services.ExamService;
import add.exam.group.services.GroupService;
import add.exam.model.exam.Exam;
import add.exam.model.group.Group;
import add.exam.model.poll.Poll;
import add.exam.model.user.User;
import add.exam.poll.services.PollService;
import add.exam.user.services.LoginService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.inject.Inject;
import java.util.List;

@Controller
@RequestMapping(DashboardController.REQUEST_URL)
public class DashboardController
{
    public static final String REQUEST_URL = "/dashboard";

    @Inject
    private ExamService examService;

    @Inject
    private LoginService userService;

    @Inject
    private GroupService groupService;

    @Inject
    private PollService pollService;

    @Inject
    private DashboardHelper helper;

    //view templates
    private static final String DASHBOARD_TEACHER_TEMPLATE = "dashboard/teacher";
    private static final String DASHBOARD_STUDENT_TEMPLATE = "dashboard/student";
    private static final String DASHBOARD_ADMIN_TEMPLATE = "dashboard/admin";

    @RequestMapping ( method = RequestMethod.GET)
    public String viewDashboard(Model model){
        User user = userService.getUser();
        switch(user.getRole()){
            case ROLE_TEACHER: return viewTeacherDashboard(model);
            case ROLE_ADMIN: return viewAdminDashboard(model);
            default: return  viewStudentDashboard(model);
        }
    }

    private String viewTeacherDashboard(Model model){
        User user = userService.getUser();
        List<Exam> exams = examService.getTeacherExams(user.getId());
        List<Group> groups = groupService.getTeacherGroups(user.getId());
        List<Poll> polls = pollService.getTeacherPolls(user.getId());
        Integer studentsCount = userService.getStudentsCount(user.getId());
        helper.addTeacherDashboardAttr(exams, polls,  groups, studentsCount, model);
        return DASHBOARD_TEACHER_TEMPLATE;
    }

    private String viewAdminDashboard(Model model){
        return DASHBOARD_ADMIN_TEMPLATE;
    }

    private String viewStudentDashboard(Model model){
        User user = userService.getUser();
        List<Group> groups = groupService.getStudentGroups(user.getId());
        List<Exam> exams = examService.getStudentExams(user.getId());
        List<Poll> polls = pollService.getStudentPolls(user.getId());
        helper.addStudentDashboardAttr(exams, groups, polls, model);
        return DASHBOARD_STUDENT_TEMPLATE;
    }
}
