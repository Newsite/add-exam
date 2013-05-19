package add.exam.group.helpers;

import add.exam.model.exam.Exam;
import add.exam.model.group.Group;
import add.exam.model.user.User;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import java.util.Set;

@Component
public class GroupHelper
{
    private static final String GROUP = "group";
    private static final String EXAMS = "exams";
    private static final String EXAM = "exam";
    private static final String STUDENTS = "students";
    private static final String STUDENT = "student";
    private static final String STUDENT_NAME = "studentName";

    public void addEditAttr(Model model, Group group, Set<Exam> exams, Set<User> students){
        addGroupAttr(model, group);
        model.addAttribute(EXAMS, exams);
        model.addAttribute(STUDENTS, students);
    }

    public void addGroupAttr(Model model, Group group)
    {
        model.addAttribute(GROUP, group);
    }

    public void addExamAttr(Exam exam, Model model)
    {
        model.addAttribute(EXAM, exam);
    }

    public void addStudentAttr(User student, Model model)
    {
        model.addAttribute(STUDENT, student);
        model.addAttribute(STUDENT_NAME, String.format("%s %s", student.getFirstName(), student.getFirstName()));
    }
}
