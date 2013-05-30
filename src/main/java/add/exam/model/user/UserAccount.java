package add.exam.model.user;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table (name = "user_accounts")
public class UserAccount
{
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    @Getter
    @Setter
    private Integer id;

    @Column (nullable = false)
    @Getter
    @Setter
    private Double balance = 0D;

    @Column(name="paid_date")
    @Getter
    @Setter
    private Date paidDate;

    @Column(nullable = false, name="account_expired_date")
    @Getter
    @Setter
    private Date accountExpiredDate;

    @Column(nullable = false, name = "type")
    @Getter
    @Setter
    private UserAccountType type = UserAccountType.FREE;

    @Column(name = "exams_count")
    @Getter
    @Setter
    private Integer examsCount = 1;

    @Column(name = "polls_count")
    @Getter
    @Setter
    private Integer pollCount = 1;

    @Column(name = "students_count")
    @Getter
    @Setter
    private Integer studentsCount = 1;

    @Column(name = "attempts_count")
    @Getter
    @Setter
    private Integer attemptsCount = 10;
}
