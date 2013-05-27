package add.exam.model.attempt;

import add.exam.model.exam.Exam;
import add.exam.model.user.User;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table (name = "attempts")
public class Attempt
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
    @JoinColumn (name = "exam_id", nullable = false)
    @Getter
    @Setter
    private Exam exam;

    @Column(nullable = false, name = "start_time")
    @Getter
    @Setter
    private Date startTime;

    //time for taking exam in seconds
    @Column(name = "total_time")
    @Getter
    @Setter
    private Long totalTime;

    @Getter
    @Setter
    @Column(nullable = false, name = "current_question_number")
    private Integer currentQuestionNumber = 0;

    @Getter
    @Setter
    @Column(name = "result_score")
    private Integer resultScore;

    @Getter
    @Setter
    @Column(name = "completed", nullable = false)
    private Boolean completed = false;

    @Getter
    @Setter
    @OneToMany (mappedBy = "attempt", cascade = CascadeType.ALL)
    private List<AttemptQuestion> questions;
}
