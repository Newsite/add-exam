package add.exam.report.model;

import add.exam.model.exam.Exam;
import lombok.Getter;
import lombok.Setter;

public class ExamInfo
{
    @Getter
    @Setter
    private Exam exam;

    @Getter
    @Setter
    private Integer numberOfAttempts = 0;

    @Getter
    @Setter
    private Integer numberOfSuccessAttempts = 0;

    @Getter
    @Setter
    private Integer averageScore = 0;

    @Getter
    @Setter
    private String averageTime = "00:00:00";

}
