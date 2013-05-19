package add.exam.group.controllers;

import add.exam.common.services.CommonService;
import add.exam.exam.controllers.ExamController;
import add.exam.exam.services.ExamService;
import add.exam.group.helpers.GroupHelper;
import add.exam.group.services.GroupService;
import add.exam.model.exam.Exam;
import add.exam.model.group.Group;
import add.exam.model.user.User;
import add.exam.user.services.LoginService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.util.Set;

@Controller
@RequestMapping(GroupController.REQUEST_URL)
public class GroupController
{
    public static final String REQUEST_URL = "/group";

    //templates
    private static final String EDIT_GROUP_TEMPLATE = "group/groupForm";
    private static final String EXAM_ROW_TEMPLATE = "common/ajax-templates/examRow";
    private static final String STUDENT_ROW_TEMPLATE = "common/ajax-templates/studentRow";
    private static final String REDIRECT_TO_EDIT_GROUP_URL = "redirect:/group/%s/edit.html";

    @Inject
    private LoginService userService;

    @Inject
    private GroupService groupService;

    @Inject
    private ExamService examService;

    @Inject
    private CommonService commonService;

    @Inject
    private GroupHelper helper;

    @RequestMapping(value = "/{id}/edit", method = RequestMethod.GET)
    public String edit(@PathVariable Integer id, Model model){
        User user = userService.getUser();
        Group group = groupService.getGroup(id, user.getId());
        Set<Exam> exams = group.getExams();
        Set<User> students = group.getStudents();
        helper.addEditAttr(model, group, exams, students);
        return EDIT_GROUP_TEMPLATE;
    }

    @RequestMapping(value = "/new", method = RequestMethod.GET)
    public String edit(Model model){
        helper.addGroupAttr(model, new Group());
        return EDIT_GROUP_TEMPLATE;
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String edit(@ModelAttribute Group group){
        User user = userService.getUser();
        group.setUser(user);
        groupService.save(group);
        return String.format(REDIRECT_TO_EDIT_GROUP_URL, group.getId());
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public String delete(@PathVariable ("id") Integer groupId){
        commonService.delete(Group.class, groupId);
        return ExamController.AJAX_SUCCESS_MESSAGE_TEMPLATE;
    }

    @RequestMapping(value ="/{id}/add/exam", method = RequestMethod.POST)
    public String addExamToGroup(@PathVariable ("id") Integer groupId, @RequestParam Integer examId, Model model){
        User user = userService.getUser();
        Group group = groupService.getGroup(groupId, user.getId());
        Exam exam = examService.getExam(examId, user.getId());
        group.getExams().add(exam);
        groupService.save(group);
        helper.addExamAttr(exam, model);
        return EXAM_ROW_TEMPLATE;
    }

    @RequestMapping(value = "/{groupId}/exam/{examId}", method = RequestMethod.DELETE)
    public String removeExamFromGroup(@PathVariable Integer groupId, @PathVariable Integer examId){
        User user = userService.getUser();
        Group group = groupService.getGroup(groupId, user.getId());
        Exam exam = examService.getExam(examId, user.getId());
        if (group.getExams().remove(exam)){
            groupService.save(group);
        }
        return ExamController.AJAX_SUCCESS_MESSAGE_TEMPLATE;
    }

    @RequestMapping(value ="/{groupId}/add/student", method = RequestMethod.POST)
    public String addStudentToGroup(@PathVariable Integer groupId, @RequestParam Integer studentId, Model model){
        User user = userService.getUser();
        Group group = groupService.getGroup(groupId, user.getId());
        User student = commonService.get(User.class, studentId);
        group.getStudents().add(student);
        groupService.save(group);
        helper.addStudentAttr(student, model);
        return STUDENT_ROW_TEMPLATE;
    }

    @RequestMapping(value = "/{groupId}/student/{studentId}", method = RequestMethod.DELETE)
    public String removeStudentFromGroup(@PathVariable Integer groupId, @PathVariable Integer studentId){
        User user = userService.getUser();
        Group group = groupService.getGroup(groupId, user.getId());
        User student = commonService.get(User.class, studentId);
        if (group.getStudents().remove(student)){
            groupService.save(group);
        }
        return ExamController.AJAX_SUCCESS_MESSAGE_TEMPLATE;
    }

}
