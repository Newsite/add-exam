package add.exam.model.attempt;

import add.exam.model.exam.ExamQuestion;
import add.exam.model.exam.ExamQuestionType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table (name = "attempt_questions")
public class AttemptQuestion
{
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Getter
    @Setter
    private Integer id;

    @ManyToOne
    @JoinColumn (name = "question_id")
    @Getter
    @Setter
    private ExamQuestion question;

    @Column(columnDefinition = "text")
    @Getter
    @Setter
    private String body;

    @Column(columnDefinition = "text")
    @Getter
    @Setter
    private String explanation;

    @Column(columnDefinition = "VARCHAR(13)")
    @Enumerated(EnumType.STRING)
    @Getter
    @Setter
    private ExamQuestionType type = ExamQuestionType.MULTI_CHOICE;

    @ManyToOne
    @JoinColumn (name = "attempt_id", nullable = false)
    @Getter
    @Setter
    private Attempt attempt;

    @Column(columnDefinition = "text")
    @Getter
    @Setter
    private String answers;

    @Column
    @Getter
    @Setter
    private boolean correct;

    @Column
    @Getter
    @Setter
    private boolean marked = false;
}
