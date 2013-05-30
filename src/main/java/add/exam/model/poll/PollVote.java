package add.exam.model.poll;

import add.exam.model.exam.Exam;
import add.exam.model.user.User;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table (name = "poll_votes")
public class PollVote
{
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Getter
    @Setter
    private Integer id;

    @ManyToOne
    @JoinColumn (name = "user_id")
    @Getter
    @Setter
    private User user;

    @ManyToOne
    @JoinColumn (name = "poll_id", nullable = false)
    @Getter
    @Setter
    private Poll poll;

    @Getter
    @Setter
    @Column(nullable = false, name = "current_question_number")
    private Integer currentQuestionNumber = 0;

    @Getter
    @Setter
    @Column(name = "completed", nullable = false)
    private Boolean completed = false;
}
