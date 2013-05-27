package add.exam.user.services;

import add.exam.common.services.EmailService;
import add.exam.model.user.User;
import add.exam.model.user.UserAccount;
import add.exam.model.user.UserRole;
import add.exam.user.repositories.UserRepository;
import org.dozer.DozerBeanMapper;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class LoginService
{
    private static final Integer TRIAL_PERIOD_DAYS = 14;

    @Inject
    private UserRepository userRepository;

    @Inject
    private EmailService emailService;

    @Inject
    private DozerBeanMapper dozerMapper;

    //email templates
    private static final String REGISTER_EMAIL_TEMPLATE = "new.user.template";
    private static final String PROFILE_CHANGED_EMAIL_TEMPLATE = "profile.changed.template";

    public void create(User user){
        if (user.getRole() == UserRole.ROLE_TEACHER){
            UserAccount account = new UserAccount();
            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date());
            cal.add(Calendar.DATE, TRIAL_PERIOD_DAYS);
            account.setAccountExpiredDate(cal.getTime());
            user.setAccount(account);
        }
        user.setRegisterDate(new Date());
        userRepository.create(user);
    }

    public void update(User user, User authenticatedUser){
        user.setRole(authenticatedUser.getRole());
        dozerMapper.map(user, authenticatedUser);
        userRepository.update(authenticatedUser);
    }

    public boolean existUsername(String username){
        return userRepository.existUsername(username);
    }

    public boolean existEmail(String email, UserRole role, Integer userId){
        return userRepository.existEmail(email, role, userId);
    }

    public User getUser()
    {
        return userRepository.getUser();
    }

    public List<User> getStudents(String pattern){
        return userRepository.getStudentsByNamePattern(pattern);
    }

    public void sendRegisterEmail(User user)
    {
        Object[] bodyParams = new Object[]{ user.getFirstName(), user.getRole().toString().replace("ROLE_", ""),
                user.getUsername(), user.getPassword()};
        emailService.sendEmail(user.getEmail(), REGISTER_EMAIL_TEMPLATE, bodyParams );
    }

    public void sendProfileChangedEmail(User user)
    {
        Object[] bodyParams = new Object[]{ user.getFirstName(), user.getLastName(),
                user.getUsername(), user.getPassword()};
        emailService.sendEmail(user.getEmail(), PROFILE_CHANGED_EMAIL_TEMPLATE, bodyParams );
    }

    public Integer getStudentsCount(Integer userId){
        return userRepository.getStudentsCount(userId);
    }
}
