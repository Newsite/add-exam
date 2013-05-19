package add.exam.common.services;

import javax.inject.Inject;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.Properties;

/**
 * Service that will send emails
 */

@Service
public class EmailService
{
    //properties names
    private static final String MAIL_SMTP_HOST = "mail.smtp.host";
    private static final String MAIL_SMTP_SOCKETFACTORY_PORT = "mail.smtp.socketFactory.port";
    private static final String MAIL_SMTP_SOCKETFACTORY_CLASS = "mail.smtp.socketFactory.class";
    private static final String MAIL_SMTP_AUTH = "mail.smtp.auth";
    private static final String MAIL_SMTP_PORT = "mail.smtp.port";

    //properties values
    private static final String MAIL_SMTP_AUTH_VALUE = "true";
    private static final Integer MAIL_SMTP_SOCKETFACTORY_PORT_VALUE = 465;
    private static final String MAIL_SMTP_SOCKETFACTORY_CLASS_VALUE = "javax.net.ssl.SSLSocketFactory";
    private static final String MAIL_SMTP_HOST_VALUE = "smtp.gmail.com";
    private static final String MAIL_SMTP_USER_VALUE = "goroshkevych@gmail.com";
    private static final String MAIL_SMTP_PASSWORD_VALUE = "78911152";

    //email templates file names
    private static final String EMAIL_TEMPLATES_FILENAME = "email-templates.properties";

    private static final String STRING_PATTERN = "%s%s";
    private static final String SUBJECT = ".subject";

    @Inject
    private PropertiesService propertiesService;

    /**
     * Send a single email.
     */
    private void sendEmail( String toEmail, String body, String subject )
    {
        Properties props = new Properties();
        props.put(MAIL_SMTP_HOST, MAIL_SMTP_HOST_VALUE);
        props.put(MAIL_SMTP_SOCKETFACTORY_PORT, MAIL_SMTP_SOCKETFACTORY_PORT_VALUE);
        props.put(MAIL_SMTP_SOCKETFACTORY_CLASS, MAIL_SMTP_SOCKETFACTORY_CLASS_VALUE);
        props.put(MAIL_SMTP_AUTH, MAIL_SMTP_AUTH_VALUE);
        props.put(MAIL_SMTP_PORT, MAIL_SMTP_SOCKETFACTORY_PORT_VALUE);

        Session session = Session.getInstance( props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication( MAIL_SMTP_USER_VALUE, MAIL_SMTP_PASSWORD_VALUE);
            }
        });

        MimeMessage message = new MimeMessage( session );
        try{
            message.setFrom( new InternetAddress( MAIL_SMTP_USER_VALUE ) );
            message.addRecipient( Message.RecipientType.TO, new InternetAddress(toEmail) );
            message.setSubject( subject );
            message.setContent( body, "text/html" );
            Transport.send(message);
        }
        catch (MessagingException ex){

        }
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

}
