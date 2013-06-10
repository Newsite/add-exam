package add.exam.common.helpers;

import add.exam.model.exam.Exam;
import add.exam.model.poll.Poll;
import add.exam.model.user.User;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import java.util.*;

@Component
public class SearchHelper
{
    private static final String FULL_USERNAME_PATTERN = "%s %s";

    //attributes
    private static final String OPTIONS = "options";

    public void addStudentOptions(List<User> users, Model model){
        model.addAttribute(OPTIONS, getStudentNames(users));
    }

    public void addExamOptions(List<Exam> exams, Model model)
    {
        model.addAttribute(OPTIONS, getExamNames(exams));
    }

    public void addPollOptions(List<Poll> polls, Model model)
    {
        model.addAttribute(OPTIONS, getPollNames(polls));
    }

    private Map<Integer, String> getPollNames(List<Poll> polls)
    {
        Map<Integer, String> result = new HashMap<Integer, String>();
        for (Poll poll: polls){
            result.put(poll.getId(), poll.getName());
        }
        return result;
    }

    private Map<Integer, String> getStudentNames(List<User> users){
        Map<Integer, String> result = new HashMap<Integer, String>();
        for (User user: users){
            result.put(user.getId(), String.format(FULL_USERNAME_PATTERN, user.getFirstName(), user.getLastName()));
        }
        return result;
    }

    private Map<Integer, String> getExamNames(List<Exam> exams){
        Map<Integer, String> result = new HashMap<Integer, String>();
        for (Exam exam: exams){
            result.put(exam.getId(), exam.getName());
        }
        return result;
    }

    public List<Exam> excludeAddedExams(Set<Exam> groupExams, List<Exam> userExams)
    {
        List<Exam> result = new ArrayList<Exam>();
        for (Exam exam: userExams){
            if (!groupExams.contains(exam)){
                result.add(exam);
            }
        }
        return result;
    }

    public List<User> excludeAddedStudents(Set<User> groupStudents, List<User> students)
    {
        List<User> result = new ArrayList<User>();
        for(User student: students){
            if(!groupStudents.contains(student)){
                result.add(student);
            }
        }
        return result;
    }

    public List<Poll> excludeAddedPolls(Set<Poll> groupPolls, List<Poll> userPolls)
    {
        List<Poll> result = new ArrayList<Poll>();
        for (Poll poll: userPolls){
            if (!groupPolls.contains(poll)){
                result.add(poll);
            }
        }
        return result;
    }
}
