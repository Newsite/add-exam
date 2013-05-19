package add.exam.model.user;


import add.exam.model.attempt.Attempt;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity
@Table (name = "users")
public class User
{
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    @Getter
    @Setter
    private Integer id;

    @Column(nullable = false)
    @Getter
    @Setter
    private String email;

    @Column(nullable = false, name = "first_name")
    @Getter
    @Setter
    private String firstName;

    @Column(nullable = false, name="last_name")
    @Getter
    @Setter
    private String lastName;

    @Column(nullable = false)
    @Getter
    @Setter
    private String username;

    @Column(nullable = false)
    @Getter
    @Setter
    private String password;

    @Column(name="account_expired")
    @Getter
    @Setter
    private Date accountExpired;

    @Column(name="register_date")
    @Getter
    @Setter
    private Date registerDate;

    @Column(nullable = false)
    @Getter
    @Setter
    private Double balance = 0D;

    @Getter
    @Setter
    @Column(nullable = false, columnDefinition = "VARCHAR(20)")
    @Enumerated(EnumType.STRING)
    private UserRole role = UserRole.ROLE_STUDENT;

    @Getter
    @Setter
    @OneToMany (mappedBy = "exam", cascade = CascadeType.ALL)
    private List<Attempt> attempts;

    @Override
    public boolean equals(Object obj) {
        if (obj == null || obj.getClass() != this.getClass()){
            return false;
        }
        User user = (User) obj;
        return Objects.equals(user.getId(), id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
