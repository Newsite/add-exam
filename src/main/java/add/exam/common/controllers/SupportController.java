package add.exam.common.controllers;

import add.exam.common.services.EmailService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.inject.Inject;

@Controller
@RequestMapping(SupportController.REQUEST_URL)
public class SupportController
{
    public static final String REQUEST_URL = "/support";
    //private static final String

    //templates
    private static final String SUPPORT_FORM_TEMPLATE = "common/support";

    @Inject
    private EmailService emailService;

    @RequestMapping(method = RequestMethod.GET)
    public String showSupportPage(){
        return SUPPORT_FORM_TEMPLATE;
    }

    @RequestMapping(method = RequestMethod.POST)
    public String sendEmailToSupport(@RequestParam String email, @RequestParam String description,
                                     @RequestParam String subject, @RequestParam CommonsMultipartFile file,
                                     Model model){
        emailService.sendEmail(email, EmailService.MAIL_SMTP_USER_VALUE, subject, description, file);
        model.addAttribute( "emailSend", true);
        return SUPPORT_FORM_TEMPLATE;
    }
}
