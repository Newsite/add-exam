package add.exam.user.repositories;

import add.exam.model.user.User;
import add.exam.model.user.UserRole;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigInteger;
import java.util.List;

@Repository
public class UserRepository
{
    //queries
    private static final String GET_BY_USERNAME_QUERY = "select u from User u where username = :username";
    private static final String GET_BY_EMAIL_QUERY = "select u from User u where email = :email and role = :role";
    private static final String GET_USER_BY_NAME_PATTERN_QUERY = "select u from User u where role = :role and" +
            " (first_name LIKE :name or last_name LIKE :name or username LIKE :name)";
    private static final String COUNT_TEACHER_STUDENTS_NATIVE_QUERY = "select count(distinct s) from groups as g " +
            "left join students_in_groups as sig on g.id = sig.group_id left join users as s on sig.student_id = s.id " +
            "where g.user_id = :userId";

    //parameters
    private static final String EMAIL = "email";
    private static final String USERNAME = "username";
    private static final String NAME = "name";
    private static final String ROLE = "role";
    private static final String USER_ID = "userId";


    @PersistenceContext
    private EntityManager entityManager;

    @Inject
    private StandardPasswordEncoder encoder;

    @Transactional (readOnly = true)
    public boolean existUsername(String username){
        List<User> users = entityManager.createQuery(GET_BY_USERNAME_QUERY, User.class)
                .setParameter(USERNAME, username)
                .getResultList();
        return !users.isEmpty();
    }

    @Transactional (readOnly = true)
    public boolean existEmail(String email, UserRole role, Integer userId){
        List<User> users = entityManager.createQuery(GET_BY_EMAIL_QUERY, User.class)
                .setParameter(EMAIL, email)
                .setParameter(ROLE, role)
                .getResultList();
        for (User user: users){
            if (!user.getId().equals(userId)){
                return true;
            }
        }
        return false;
    }

    /**
     * creates(register) new user
     * @param user
     *          user data
     */
    @Transactional
    public void create(User user)
    {
        String encodedPassword = encoder.encode(user.getPassword());
        user.setEncodedPassword(encodedPassword);
        entityManager.persist(user);
    }

    @Transactional (readOnly = true)
    public User getUser()
    {   String username;
        try{
            username = SecurityContextHolder.getContext().getAuthentication().getName();
        }catch(NullPointerException ex){
            //user is not authenticated
            return null;
        }
        List<User> users = entityManager.createQuery(GET_BY_USERNAME_QUERY, User.class)
                .setParameter(USERNAME, username)
                .getResultList();
        if (users.size() == 0){
            return null;
        }
        return users.get(0);
    }

    @Transactional(readOnly = true)
    public List<User> getStudentsByNamePattern(String pattern){
        return entityManager.createQuery(GET_USER_BY_NAME_PATTERN_QUERY, User.class)
                .setParameter(ROLE, UserRole.ROLE_STUDENT)
                .setParameter(NAME, "%" + pattern + "%")
                .getResultList();
    }

    @Transactional
    public void update(User user)
    {
        String encodedPassword = encoder.encode(user.getPassword());
        user.setEncodedPassword(encodedPassword);
        entityManager.merge(user);
    }

    @Transactional(readOnly = true)
    public Integer getStudentsCount(Integer userId){
        BigInteger val = (BigInteger) entityManager.createNativeQuery(COUNT_TEACHER_STUDENTS_NATIVE_QUERY)
                .setParameter(USER_ID, userId)
                .getSingleResult();
        return val.intValue();
    }
}
