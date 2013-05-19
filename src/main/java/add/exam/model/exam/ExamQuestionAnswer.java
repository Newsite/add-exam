package add.exam.model.exam;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table (name="exam_question_answers")
public class ExamQuestionAnswer
{
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Getter
    @Setter
    private Integer id;

    @Column(nullable = false, columnDefinition = "text")
    @Getter
    @Setter
    private String body;

    @Column(nullable = false)
    @Getter
    @Setter
    private boolean correct = false;

    @ManyToOne
    @JoinColumn (name = "question_id")
    @Getter
    @Setter
    private ExamQuestion question;
}
