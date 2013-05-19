package add.exam.model.group;

import add.exam.model.exam.Exam;
import add.exam.model.user.User;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table (name = "groups")
public class Group
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

    @Column(nullable = false, columnDefinition = "text")
    @Getter
    @Setter
    private String description;

    @Getter
    @Setter
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "students_in_groups",
            joinColumns = { @JoinColumn(name = "group_id") },
            inverseJoinColumns = { @JoinColumn(name = "student_id") })
    private Set<User> students;

    @Getter
    @Setter
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "exams_in_groups",
            joinColumns = { @JoinColumn(name = "group_id") },
            inverseJoinColumns = { @JoinColumn(name = "exam_id") })
    private Set<Exam> exams;
}
