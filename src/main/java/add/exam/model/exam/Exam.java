package add.exam.model.exam;

import add.exam.model.attempt.Attempt;
import add.exam.model.user.User;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Table (name = "exams")
public class Exam
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

    @Column(name = "questions_count", nullable = false)
    @Getter
    @Setter
    private Integer questionsCount;

    @Column(name = "pass_score", nullable = false)
    @Getter
    @Setter
    private Integer passScore;

    @Column(nullable = false, columnDefinition = "VARCHAR(10)")
    @Enumerated(EnumType.STRING)
    @Getter
    @Setter
    private ExamAccessType access = ExamAccessType.PUBLIC;

    @Column(name = "total_time")
    @Getter
    @Setter
    private Integer totalTime;

    @Column(nullable = false)
    @Getter
    @Setter
    private Boolean published = false;

    @Getter
    @Setter
    @OneToOne (cascade = CascadeType.ALL)
    private ExamSettings settings;

    @Getter
    @Setter
    @OneToMany (mappedBy = "exam", cascade = CascadeType.ALL)
    private List<ExamQuestion> questions;

    @Getter
    @Setter
    @OneToMany (mappedBy = "exam", cascade = CascadeType.ALL)
    private List<Attempt> attempts;

    @Override
    public boolean equals(Object obj) {
        if (obj == null || obj.getClass() != this.getClass()){
            return false;
        }
        Exam exam = (Exam) obj;
        return Objects.equals(exam.getId(), id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
