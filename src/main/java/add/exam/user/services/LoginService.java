package add.exam.user.services;

import add.exam.common.services.EmailService;
import add.exam.exceptions.ResourceNotFoundException;
import add.exam.model.user.User;
import add.exam.user.repositories.UserRepository;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

@Service
public class LoginService
{
    @Inject
    private UserRepository userRepository;

    @Inject
    private EmailService emailService;

    private static final String REGISTER_EMAIL_TEMPLATE = "new.user.template";

    public void create(User user){
        userRepository.create(user);
    }

    public boolean existUsername(String username){
        return userRepository.existUsername(username);
    }

    public boolean existEmail(String email){
        return userRepository.existEmail(email);
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
        Object[] bodyParams = new Object[]{ user.getFirstName(), user.getUsername(), user.getPassword()};
        emailService.sendEmail(user.getEmail(), REGISTER_EMAIL_TEMPLATE, bodyParams );
    }
}
