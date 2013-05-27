package add.exam.user.controllers;

import add.exam.model.user.User;
import add.exam.user.helpers.UserHelper;
import add.exam.user.services.LoginService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.inject.Inject;

/**
 * this controller process requests which are related to login, register user profiles
 */
@Controller
public class UserController
{
    //urls
    private static final String REDIRECT_TO_LOGIN_FORM = "redirect:/login.html";
    private static final String REDIRECT_TO_PROFILE_FORM = "redirect:/profile.html";
    private static final String REGISTER_USER_URL = "/register.html";
    private static final String SAVE_PROFILE_URL = "/profile.html";

    //templates
    private static final String LOGIN_FORM_TEMPLATE = "login/loginForm";
    private static final String PROFILE_FORM_TEMPLATE = "login/profileForm";

    //messages
    private static final String USERNAME_ALREADY_EXIST_ERROR_MSG = "User with entered username already exist!";
    private static final String EMAIL_ALREADY_EXIST_ERROR_MSG = "User with entered email already exist!";

    @Inject
    private LoginService service;

    @Inject
    private UserHelper helper;

    /**
     * handle page for user login, if user is authenticated redirects to user profile
     * @return
     *      login form
     */
    @RequestMapping(method = RequestMethod.GET, value="/login")
    public String login(){
        User authenticatedUser = service.getUser();
        if (authenticatedUser != null){
            return REDIRECT_TO_PROFILE_FORM;
        }
        return LOGIN_FORM_TEMPLATE;
    }

    /**
     * handle page for unsuccessful user login, if user is authenticated redirects to user profile
     * @return
     *      login form with error
     */
    @RequestMapping("/login-error.html")
    public String loginError(Model model) {
        User authenticatedUser = service.getUser();
        if (authenticatedUser != null){
            return REDIRECT_TO_PROFILE_FORM;
        }
        helper.addLoginFormError(model);
        return LOGIN_FORM_TEMPLATE;
    }

    /**
     * handle register request, shows register form, if user is authenticated redirects to user profile
     * @param model
     *          page model
     * @return
     *      register form
     */
    @RequestMapping(method = RequestMethod.GET, value="/register")
    public String showRegisterForm(Model model){
        User authenticatedUser = service.getUser();
        if (authenticatedUser != null){
            return REDIRECT_TO_PROFILE_FORM;
        }
        helper.addProfileAttributes(new User(), REGISTER_USER_URL, model);
        return PROFILE_FORM_TEMPLATE;
    }

    /**
     * this post request saves new user, if user already exist returns back to register form
     * @param user
     *          new user object
     * @param model
     *          page modal
     * @return
     *          login form if user was registered, return back to register form if error occur
     */
    @RequestMapping(method = RequestMethod.POST, value="/register")
    public String register(@ModelAttribute User user, Model model){
        User authenticatedUser = service.getUser();
        if (authenticatedUser != null){
            return REDIRECT_TO_PROFILE_FORM;
        }
        boolean userCanBeSaved = true;
        if (service.existUsername(user.getUsername())){
            helper.addRegisterFormError(model, USERNAME_ALREADY_EXIST_ERROR_MSG);
            userCanBeSaved = false;
        }
        if (service.existEmail(user.getEmail(), user.getRole(), null)){
            helper.addRegisterFormError(model, EMAIL_ALREADY_EXIST_ERROR_MSG);
            userCanBeSaved = false;
        }
        if (!userCanBeSaved){
            helper.addProfileAttributes(user, REGISTER_USER_URL, model);
            return PROFILE_FORM_TEMPLATE;
        }
        service.create(user);
        service.sendRegisterEmail(user);
        return REDIRECT_TO_LOGIN_FORM;
    }

    /**
     * shows edit user profile form
     * @param model
     *          page modal
     * @return
     *      profile form or login form if user is not authenticated
     */
    @RequestMapping(value = "/profile", method = RequestMethod.GET)
    public String profile(Model model){
        User user = service.getUser();
        if (user == null){
            return REDIRECT_TO_LOGIN_FORM;
        }
        helper.addProfileAttributes(user, SAVE_PROFILE_URL, model);
        return PROFILE_FORM_TEMPLATE;
    }

    /**
     * saves user profile form
     * @param model
     *          page modal
     * @param user
     *          user data
     * @return
     *      profile form
     */
    @RequestMapping(value = "/profile", method = RequestMethod.POST)
    public String saveProfile(Model model, User user){
        User authenticatedUser = service.getUser();
        if (authenticatedUser == null){
            return REDIRECT_TO_LOGIN_FORM;
        }
        boolean userCanBeSaved = true;
        if (service.existEmail(user.getEmail(), authenticatedUser.getRole(), authenticatedUser.getId())){
            helper.addRegisterFormError(model, EMAIL_ALREADY_EXIST_ERROR_MSG);
            userCanBeSaved = false;
            helper.addProfileAttributes(user, SAVE_PROFILE_URL, model);
        }
        //save profile
        if (userCanBeSaved){
            service.update(user, authenticatedUser);
            helper.addProfileSavedAttr(model);
            service.sendProfileChangedEmail(authenticatedUser);
            helper.addProfileAttributes(authenticatedUser, SAVE_PROFILE_URL, model);
        }
        return PROFILE_FORM_TEMPLATE;
    }

}
