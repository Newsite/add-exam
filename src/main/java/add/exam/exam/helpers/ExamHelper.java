package add.exam.exam.helpers;

import add.exam.model.exam.Exam;
import add.exam.model.exam.ExamAccessType;
import add.exam.model.exam.ExamQuestion;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import java.util.List;

@Component
public class ExamHelper
{
    //attribute names
    private static final String EXAM = "exam";
    private static final String ACCESS_TYPES = "accessTypes";
    private static final String QUESTIONS = "questions";
    private static final String NOT_COMPLETED_MESSAGE = "notCompletedMsg";
    private static final String NOT_ENOUGH_COMPLETED_QUESTION_ERROR_MSG = "Number of completed question should be not less than number of exam questions!";
    private static final String IS_COMPLETED_EXAM = "isCompletedExam" ;
    private static final String CAN_TAKE_EXAM = "canTakeExam";
    private static final String TAKE_EXAM_ERROR = "takeExamError";
    private static final String SHOW_UNPUBLISH_EXAM_BUTTON = "showUnPublishExamButton";
    private static final String EDIT_EXAM_ERROR = "editExamError";
    private static final String SOMEONE_TAKES_EXAM_AT_THE_MOMENT_ERROR_MSG = "Someone takes exam at the moment. Please wait.";

    //error messages
    private static final String NOT_PUBLISHED_EXAM_ERROR_MSG = "Exam is not published by teacher.";
    private static final String EXAM_IS_PUBLISHED_ERROR_MSG = "Exam is published. You can not edit published exams.";

    public void addExamAndTypesToModel(Exam exam, Model model){
        addExamToModel(exam, model);
        model.addAttribute(ACCESS_TYPES, ExamAccessType.getAccessTypes());
    }

    public void addEditExamDataToModel(Exam exam, List<ExamQuestion> questions, boolean isExamCompleted, Model model){
        addExamAndTypesToModel(exam, model);
        addCompletedExamAttributes(isExamCompleted, model);
        model.addAttribute(QUESTIONS, questions);
    }

    public void addCompletedExamAttributes(boolean isExamCompleted, Model model){
        if (isExamCompleted){
            model.addAttribute(IS_COMPLETED_EXAM, true);
        }else{
            model.addAttribute(NOT_COMPLETED_MESSAGE, NOT_ENOUGH_COMPLETED_QUESTION_ERROR_MSG);
        }
    }

    public void addExamToModel(Exam exam, Model model){
        model.addAttribute(EXAM, exam);
    }

    public void addTakeExamErrors(Exam exam, Model model)
    {
        if (exam.getPublished()){
            model.addAttribute( CAN_TAKE_EXAM, true);
        }else{
            model.addAttribute(TAKE_EXAM_ERROR, NOT_PUBLISHED_EXAM_ERROR_MSG);
        }
    }

    public void addClosedExamEditingErrors(Exam exam, Long activeAttemptNumber, Model model){
        if (exam.getPublished()){
            model.addAttribute(EDIT_EXAM_ERROR, EXAM_IS_PUBLISHED_ERROR_MSG);
            model.addAttribute(SHOW_UNPUBLISH_EXAM_BUTTON, true);
        }else{
            if (activeAttemptNumber > 0){
                model.addAttribute(EDIT_EXAM_ERROR, SOMEONE_TAKES_EXAM_AT_THE_MOMENT_ERROR_MSG);
            }
        }
        model.addAttribute(EXAM, exam);
    }
}
