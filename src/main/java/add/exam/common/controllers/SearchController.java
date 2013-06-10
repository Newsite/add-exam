package add.exam.common.controllers;

import add.exam.common.helpers.SearchHelper;
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
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.util.List;

@Controller
@RequestMapping(SearchController.REQUEST_URL)
public class SearchController
{
    public static final String REQUEST_URL = "/search";

    //templates
    private static final String SELECT_OPTIONS_TEMPLATE = "common/ajax-templates/selectOptions";

    @Inject
    private LoginService userService;

    @Inject
    private ExamService examService;

    @Inject
    private GroupService groupService;

    @Inject
    private PollService pollService;

    @Inject
    private SearchHelper helper;

    @RequestMapping(value = "/students", method = RequestMethod.POST)
    public String getStudents(@RequestParam Integer groupId, @ModelAttribute String pattern, Model model){
        User user = userService.getUser();
        Group group = groupService.getGroup(groupId, user.getId());
        List<User> students = userService.getStudents(pattern);
        students = helper.excludeAddedStudents(group.getStudents(), students);
        helper.addStudentOptions(students, model);
        return SELECT_OPTIONS_TEMPLATE;
    }

    @RequestMapping(value = "/exams", method = RequestMethod.POST)
    public String getExams(@RequestParam Integer groupId, @RequestParam String pattern, Model model){
        User user = userService.getUser();
        Group group = groupService.getGroup(groupId, user.getId());
        List<Exam> exams = examService.getExams(pattern, user.getId());
        exams = helper.excludeAddedExams(group.getExams(), exams);
        helper.addExamOptions(exams, model);
        return SELECT_OPTIONS_TEMPLATE;
    }

    @RequestMapping(value = "/polls", method = RequestMethod.POST)
    public String getPolls(@RequestParam Integer groupId, @RequestParam String pattern, Model model){
        User user = userService.getUser();
        Group group = groupService.getGroup(groupId, user.getId());
        List<Poll> polls = pollService.getPolls(pattern, user.getId());
        polls = helper.excludeAddedPolls(group.getPolls(), polls);
        helper.addPollOptions(polls, model);
        return SELECT_OPTIONS_TEMPLATE;
    }

}
