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

    @Column(name="company_name")
    @Getter
    @Setter
    private String companyName;

    @Column(nullable = false)
    @Getter
    @Setter
    private String username;

    @Column(nullable = false, name = "encoded_password")
    @Getter
    @Setter
    private String encodedPassword;

    @Column(nullable = false)
    @Getter
    @Setter
    private String password;

    @Column(name="register_date")
    @Getter
    @Setter
    private Date registerDate;

    @Getter
    @Setter
    @Column(nullable = false, columnDefinition = "VARCHAR(20)")
    @Enumerated(EnumType.STRING)
    private UserRole role = UserRole.ROLE_STUDENT;

    @Column(nullable = false)
    @Getter
    @Setter
    private Boolean enabled = true;

    @Getter
    @Setter
    @OneToMany (mappedBy = "user", cascade = CascadeType.ALL)
    private List<Attempt> attempts;

    @Getter
    @Setter
    @OneToOne (cascade = CascadeType.ALL)
    private UserAccount account;

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
