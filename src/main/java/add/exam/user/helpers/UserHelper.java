package add.exam.user.helpers;

import add.exam.model.user.User;
import add.exam.model.user.UserRole;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

@Component
public class UserHelper
{
    //attributes
    private static final String LOGIN_ERROR = "loginError";
    private static final String REGISTER_ERROR = "registerError";
    private static final String USER = "user";
    private static final String ROLES = "roles";
    private static final String URL = "url";
    private static final String PROFILE_SAVED = "profileSaved";

    //messages
    private static final String PROFILE_SAVED_MESSAGE = "Your profile information was saved";

    public void addLoginFormError(Model model){
        model.addAttribute(LOGIN_ERROR, true);
    }

    public void addRegisterFormError(Model model, String errorMsg){
        model.addAttribute(REGISTER_ERROR, errorMsg);
    }

    public void addProfileAttributes(User user, String url, Model model){
        model.addAttribute(USER, user);
        model.addAttribute(URL, url);
        model.addAttribute(ROLES, UserRole.getUserRoles());
    }

    public void addProfileSavedAttr(Model model)
    {
        model.addAttribute(PROFILE_SAVED, PROFILE_SAVED_MESSAGE);
    }
}
