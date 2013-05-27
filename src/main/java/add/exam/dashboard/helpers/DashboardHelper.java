package add.exam.dashboard.helpers;

import add.exam.model.exam.Exam;
import add.exam.model.group.Group;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import java.util.List;

@Component
public class DashboardHelper
{
    //attributes
    private static final String EXAMS = "exams";
    private static final String GROUPS = "groups";
    private static final String STUDENTS_COUNT = "studentsCount";

    public void addTeacherDashboardAttr(List<Exam> exams, List<Group> groups, Integer studentsCount, Model model){
        model.addAttribute(EXAMS, exams);
        model.addAttribute(GROUPS, groups);
        model.addAttribute(STUDENTS_COUNT, studentsCount);
    }

    public void addStudentDashboardAttr(List<Exam> exams, List<Group> groups, Model model)
    {
        model.addAttribute(EXAMS, exams);
        model.addAttribute(GROUPS, groups);
    }
}
