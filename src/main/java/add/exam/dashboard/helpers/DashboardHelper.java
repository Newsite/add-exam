package add.exam.dashboard.helpers;

import add.exam.model.exam.Exam;
import add.exam.model.group.Group;
import add.exam.model.poll.Poll;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import java.util.List;

@Component
public class DashboardHelper
{
    //attributes
    private static final String EXAMS = "exams";
    private static final String POLLS = "polls";
    private static final String GROUPS = "groups";
    private static final String STUDENTS_COUNT = "studentsCount";

    public void addTeacherDashboardAttr(List<Exam> exams, List<Poll> polls, List<Group> groups, Integer studentsCount, Model model){
        addExamsGroupsPolls(exams, groups, polls, model);
        model.addAttribute(STUDENTS_COUNT, studentsCount);
    }

    public void addStudentDashboardAttr(List<Exam> exams, List<Group> groups, List<Poll> polls, Model model)
    {
        addExamsGroupsPolls(exams, groups, polls, model);
    }

    private void addExamsGroupsPolls(List<Exam> exams, List<Group> groups, List<Poll> polls, Model model){
        model.addAttribute(EXAMS, exams);
        model.addAttribute(GROUPS, groups);
        model.addAttribute(POLLS, polls);
    }
}
