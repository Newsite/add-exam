package add.exam.poll.controllers;

import add.exam.attempt.forms.AttemptQuestionForm;
import add.exam.attempt.helpers.AttemptHelper;
import add.exam.attempt.services.AttemptService;
import add.exam.common.services.CommonService;
import add.exam.exam.controllers.ExamController;
import add.exam.model.attempt.Attempt;
import add.exam.model.attempt.AttemptQuestion;
import add.exam.model.exam.Exam;
import add.exam.model.user.User;
import add.exam.user.services.LoginService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.inject.Inject;
import java.util.List;

@Controller
public class PollVoteController
{
    private static final String REQUEST_MAPPING_NEW_ATTEMPT = "/poll/{id}";

}
