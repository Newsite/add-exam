package add.exam.exam.controllers;

import add.exam.common.services.CommonService;
import add.exam.exam.helpers.ExamQuestionAnswerHelper;
import add.exam.exam.helpers.ExamQuestionHelper;
import add.exam.exam.services.ExamService;
import add.exam.model.exam.ExamQuestion;
import add.exam.model.exam.ExamQuestionAnswer;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.inject.Inject;
import java.util.List;

@Controller
@RequestMapping(ExamQuestionAnswerController.REQUEST_MAPPING)
public class ExamQuestionAnswerController
{
    public static final String REQUEST_MAPPING = "/exam/{exam_id}/question/{question_id}/answer/";

    @Inject
    private ExamService examService;

    @Inject
    private CommonService commonService;

    @Inject
    private ExamQuestionHelper questionHelper;

    @Inject
    private ExamQuestionAnswerHelper helper;

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String save(@PathVariable("exam_id") Integer examId,
                                   @PathVariable ("question_id") Integer questionId,
                                   @ModelAttribute ExamQuestionAnswer answer){
        ExamQuestion question = commonService.get(ExamQuestion.class, questionId);
        answer.setQuestion(question);
        examService.save(answer);
        List<ExamQuestionAnswer> answers = examService.getExamQuestionAnswers(questionId);
        answers = helper.replaceAnswer(answers, answer);
        boolean isCompleted = questionHelper.isQuestionCompleted(answers, question.getType());
        question.setCompleted(isCompleted);
        examService.save(question);
        examService.setExamPublished(question.getExam());
        return String.format(ExamQuestionController.REDIRECT_TO_EDIT_QUESTION_URL, examId, questionId);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public String delete(@PathVariable ("question_id") Integer questionId,
                         @PathVariable ("id") Integer answerId){
        commonService.delete(ExamQuestionAnswer.class, answerId);
        ExamQuestion question = commonService.get(ExamQuestion.class, questionId);
        saveQuestionCompleted(question);
        return ExamController.AJAX_SUCCESS_MESSAGE_TEMPLATE;
    }

    private void saveQuestionCompleted(ExamQuestion question){
        List<ExamQuestionAnswer> answers = examService.getExamQuestionAnswers(question.getId());
        boolean isCompleted = questionHelper.isQuestionCompleted(answers, question.getType());
        question.setCompleted(isCompleted);
        examService.save(question);
    }
}
