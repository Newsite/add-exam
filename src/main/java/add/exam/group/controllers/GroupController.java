package add.exam.group.controllers;

import add.exam.common.services.CommonService;
import add.exam.exam.controllers.ExamController;
import add.exam.exam.services.ExamService;
import add.exam.group.helpers.GroupHelper;
import add.exam.group.services.GroupService;
import add.exam.model.exam.Exam;
import add.exam.model.group.Group;
import add.exam.model.poll.Poll;
import add.exam.model.user.User;
import add.exam.poll.services.PollService;
import add.exam.user.services.LoginService;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;

@Controller
@RequestMapping(GroupController.REQUEST_URL)
public class GroupController
{
    public static final String REQUEST_URL = "/group";
    private static final String REDIRECT_TO_EDIT_GROUP_URL = "redirect:/group/%s/edit.html";

    //templates
    private static final String EDIT_GROUP_TEMPLATE = "group/groupForm";
    private static final String VIEW_GROUP_TEMPLATE = "group/viewGroup";
    private static final String EXAM_ROW_TEMPLATE = "common/ajax-templates/examRow";
    private static final String POLL_ROW_TEMPLATE = "common/ajax-templates/pollRow";
    private static final String STUDENT_ROW_TEMPLATE = "common/ajax-templates/studentRow";


    @Inject
    private LoginService userService;

    @Inject
    private GroupService groupService;

    @Inject
    private ExamService examService;

    @Inject
    private PollService pollService;

    @Inject
    private CommonService commonService;

    @Inject
    private GroupHelper helper;

    @RequestMapping(value = "/{id}/edit", method = RequestMethod.GET)
    public String edit(@PathVariable Integer id, Model model){
        User user = userService.getUser();
        Group group = groupService.getGroup(id, user.getId());
        helper.addEditAttr(model, group);
        return EDIT_GROUP_TEMPLATE;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String viewGroup(@PathVariable Integer id, Model model){
        User user = userService.getUser();
        Group group = commonService.get(Group.class, id);
        group = groupService.getGroup(id, group.getUser().getId());
        if (!group.getStudents().contains(user)){
            throw new AccessDeniedException("You are not member of this group");
        }
        helper.addEditAttr(model, group);
        return VIEW_GROUP_TEMPLATE;
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

    @RequestMapping(value = "/{groupId}/poll/{pollId}", method = RequestMethod.DELETE)
    public String removePollFromGroup(@PathVariable Integer groupId, @PathVariable Integer pollId){
        User user = userService.getUser();
        Group group = groupService.getGroup(groupId, user.getId());
        Poll poll = pollService.getPoll(pollId, user.getId());
        if (group.getPolls().remove(poll)){
            groupService.save(group);
        }
        return ExamController.AJAX_SUCCESS_MESSAGE_TEMPLATE;
    }

    @RequestMapping(value ="/{id}/add/poll", method = RequestMethod.POST)
    public String addPollToGroup(@PathVariable ("id") Integer groupId, @RequestParam Integer pollId, Model model){
        User user = userService.getUser();
        Group group = groupService.getGroup(groupId, user.getId());
        Poll poll = pollService.getPoll(pollId, user.getId());
        group.getPolls().add(poll);
        groupService.save(group);
        helper.addPollAttr(poll, model);
        return POLL_ROW_TEMPLATE;
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
