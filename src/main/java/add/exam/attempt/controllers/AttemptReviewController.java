package add.exam.attempt.controllers;

import add.exam.attempt.helpers.AttemptReviewHelper;
import add.exam.attempt.model.UserAnswer;
import add.exam.attempt.services.AttemptService;
import add.exam.model.attempt.AttemptQuestion;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.inject.Inject;
import java.util.List;

@Controller
@RequestMapping(value = AttemptReviewController.REQUEST_URL)
public class AttemptReviewController
{
    public static final String REQUEST_URL = "/attempt/{id}/review/{question_number}";

    //templates
    private static final String REVIEW_TEMPLATE = "/attempt/review";

    @Inject
    private AttemptService attemptService;

    @Inject
    private AttemptReviewHelper helper;

    @RequestMapping(method = RequestMethod.GET)
    public String review(@PathVariable Integer id, @PathVariable("question_number") Integer questionNumber, Model model){
        AttemptQuestion question = attemptService.getCurrentQuestion(id, questionNumber - 1);
        List<UserAnswer> answers = attemptService.getUserAnswer(question);
        helper.addReviewAttr(questionNumber, question, answers, model);
        return REVIEW_TEMPLATE;
    }
}
