package add.exam.exam.controllers;

import add.exam.exam.helpers.DashboardHelper;
import add.exam.exam.helpers.ExamHelper;
import add.exam.exam.services.ExamService;
import add.exam.group.services.GroupService;
import add.exam.model.exam.Exam;
import add.exam.model.group.Group;
import add.exam.model.user.User;
import add.exam.user.services.LoginService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.inject.Inject;
import java.util.List;

@Controller
public class DashboardController
{
    @Inject
    private ExamService examService;

    @Inject
    private LoginService userService;

    @Inject
    private GroupService groupService;

    @Inject
    private DashboardHelper helper;

    //view templates
    private static final String DASHBOARD_TEMPLATE = "exam/dashboard";

    @RequestMapping (value = "/dashboard", method = RequestMethod.GET)
    public String viewExamDashboard(Model model){
        User user = userService.getUser();
        List<Exam> exams = examService.getTeacherExams(user.getId());
        List<Group> groups = groupService.getTeacherGroups(user.getId());
        helper.addExamListToModel(exams, groups, model);
        return DASHBOARD_TEMPLATE;
    }
}
