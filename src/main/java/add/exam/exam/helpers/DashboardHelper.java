package add.exam.exam.helpers;

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

    public void addExamListToModel(List<Exam> exams, List<Group> groups, Model model){
        model.addAttribute(EXAMS, exams);
        model.addAttribute(GROUPS, groups);
    }
}
