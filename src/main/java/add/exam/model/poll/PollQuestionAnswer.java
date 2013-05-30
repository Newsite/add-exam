package add.exam.model.poll;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table (name="poll_question_answers")
public class PollQuestionAnswer
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

    @ManyToOne
    @JoinColumn (name = "question_id")
    @Getter
    @Setter
    private PollQuestion question;

    @Column(nullable = false)
    @Getter
    @Setter
    private Integer voted = 0;

}
