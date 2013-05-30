package add.exam.model.poll;

import add.exam.model.exam.Exam;
import add.exam.model.exam.ExamQuestionAnswer;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table (name="poll_questions")
public class PollQuestion
{
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Getter
    @Setter
    private Integer id;

    @Column (nullable = false, columnDefinition = "text")
    @Getter
    @Setter
    private String body;

    @ManyToOne
    @JoinColumn (name = "poll_id")
    @Getter
    @Setter
    private Poll poll;

    @Column(nullable = false)
    @Getter
    @Setter
    private Boolean completed = false;

    @Getter
    @Setter
    @OneToMany (mappedBy = "question", cascade = CascadeType.ALL)
    private List<PollQuestionAnswer> answers;

}
