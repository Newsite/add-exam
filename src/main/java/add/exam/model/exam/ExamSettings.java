package add.exam.model.exam;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table (name = "exam_settings")
public class ExamSettings
{
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    @Getter
    @Setter
    private Integer id;

    @Column(nullable = false, name = "random_order")
    @Getter
    @Setter
    private Boolean randomOrder = true;

    @Column(nullable = false, name="show_answers")
    @Getter
    @Setter
    private Boolean showAnswers = true;

    @Column(nullable = false, name="send_email_to_teacher")
    @Getter
    @Setter
    private Boolean sendEmailToTeacher = false;

    @Column(nullable = false, name="send_email_to_student")
    @Getter
    @Setter
    private Boolean sendEmailToStudent = false;

    @Column(nullable = false, name="show_attempt_rank")
    @Getter
    @Setter
    private Boolean showAttemptRank = false;

}
