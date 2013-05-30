package add.exam.model.poll;

import add.exam.model.attempt.Attempt;
import add.exam.model.user.User;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table (name = "polls")
public class Poll
{
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    @Getter
    @Setter
    private Integer id;

    @ManyToOne
    @JoinColumn (name = "user_id", nullable = false)
    @Getter
    @Setter
    private User user;

    @Column(nullable = false, columnDefinition = "text")
    @Getter
    @Setter
    private String name;

    @Column(columnDefinition = "text")
    @Getter
    @Setter
    private String description;

    @Column(nullable = false)
    @Getter
    @Setter
    private Boolean published = false;

    @Column(nullable = false)
    @Getter
    @Setter
    private Boolean completed = false;

    @Getter
    @Setter
    @OneToMany (mappedBy = "poll", cascade = CascadeType.ALL)
    private List<PollQuestion> questions;

    @Getter
    @Setter
    @OneToMany (mappedBy = "poll", cascade = CascadeType.ALL)
    private List<PollVote> votes;

}
