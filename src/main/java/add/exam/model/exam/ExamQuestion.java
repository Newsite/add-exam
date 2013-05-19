package add.exam.model.exam;

import add.exam.model.attempt.AttemptQuestion;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table (name="exam_questions")
public class ExamQuestion
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

    @Column(columnDefinition = "text")
    @Getter
    @Setter
    private String explanation;

    @ManyToOne
    @JoinColumn (name = "exam_id")
    @Getter
    @Setter
    private Exam exam;

    @Column(columnDefinition = "VARCHAR(13)")
    @Enumerated(EnumType.STRING)
    @Getter
    @Setter
    private ExamQuestionType type = ExamQuestionType.MULTI_CHOICE;

    @Column(nullable = false, name = "random_order")
    @Getter
    @Setter
    private Boolean randomOrder = true;

    @Column(nullable = false)
    @Getter
    @Setter
    private Boolean completed = false;

    @Getter
    @Setter
    @OneToMany (mappedBy = "question", cascade = CascadeType.ALL)
    private List<ExamQuestionAnswer> answers;

    @Getter
    @Setter
    @OneToMany (mappedBy = "question", cascade = CascadeType.ALL)
    private List<AttemptQuestion> attemptQuestions;

}
