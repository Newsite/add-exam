package add.exam.common.services;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.inject.Inject;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.Properties;

/**
 * Service that will send emails
 */

@Service
public class EmailService
{
    public static final String MAIL_SMTP_USER_VALUE = "goroshkevych@gmail.com";

    //email templates file names
    private static final String EMAIL_TEMPLATES_FILENAME = "email-templates.properties";

    private static final String STRING_PATTERN = "%s%s";
    private static final String SUBJECT = ".subject";

    @Inject
    private PropertiesService propertiesService;

    @Inject
    private JavaMailSender mailSender;
    /**
     * Send a single email from addExam
     */
    private void sendEmail( String toEmail, String body, String subject )
    {
        sendEmail(MAIL_SMTP_USER_VALUE, toEmail, subject, body, null);
    }

    public void sendEmail(String toEmail, String emailTemplate, Object[] bodyParameters){
        String body = MessageFormat.format(
                propertiesService.getProperty(EMAIL_TEMPLATES_FILENAME, emailTemplate), bodyParameters);
        sendEmail(toEmail, body, getTemplateSubject(emailTemplate));
    }

    private String getTemplateSubject(String emailTemplate){
        String key = String.format(STRING_PATTERN, emailTemplate, SUBJECT);
        return propertiesService.getProperty(EMAIL_TEMPLATES_FILENAME, key);
    }

    /**
     * universal method for sending emails
     * @param fromEmail
     *          from email address
     * @param toEmail
     *          to email address
     * @param subject
     *          email subject
     * @param body
     *          email body
     * @param attachment
     *          file attachment
     */
    public void sendEmail(final String fromEmail, final String toEmail, final String subject, final String body, final CommonsMultipartFile attachment){
        mailSender.send(new MimeMessagePreparator()
        {
            @Override
            public void prepare(MimeMessage mimeMessage) throws Exception
            {
                MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true, "ISO-8859-1");
                messageHelper.setFrom(  new InternetAddress(fromEmail) );
                messageHelper.setTo( new InternetAddress(toEmail));
                messageHelper.setSubject(subject);
                messageHelper.setText( body, true );
                if (attachment != null){
                    // determines if there is an upload file, attach it to the e-mail
                    String attachName = attachment.getOriginalFilename();
                        if (!attachName.equals("")) {

                            messageHelper.addAttachment(attachName, new InputStreamSource() {

                                @Override
                                public InputStream getInputStream() throws IOException
                                {
                                    return attachment.getInputStream();
                                }
                            });
                        }
                    }
                }
            }
        );
    }

}
