package homiessecurity.service.impl;

import homiessecurity.entities.Status;
import homiessecurity.service.EmailSenderService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Map;

@RequiredArgsConstructor
@Service
public class EmailSenderServiceImpl implements EmailSenderService {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    @Override
    public void sendHtmlEmail(String toEmail, String name, String subject) throws MessagingException {

        Context context = new Context();
        context.setVariables(Map.of("name",name));
        String text = templateEngine.process("submittedEmail", context);
        MimeMessage message = getMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setPriority(1);
        helper.setSubject(subject);
        helper.setFrom("nikhilbudhathoki0@gmail.com");
        helper.setTo(toEmail);
        helper.setText(text, true);
        mailSender.send(message);
        System.out.println("Mail sent sucessfully");
    }

    private MimeMessage getMimeMessage() {
        return mailSender.createMimeMessage();
    }

    public void sendVerificationEmail(String toEmail, String name, String subject, String  token) throws MessagingException {

        Context context = new Context();
        context.setVariables(Map.of("name",name , "verificationUrl", getVerificationUrl(token)));
        String text = templateEngine.process("verificationEmail", context);
        MimeMessage message = getMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setPriority(1);
        helper.setSubject(subject);
        helper.setFrom("nikhilbudhathoki0@gmail.com");
        helper.setTo(toEmail);
        helper.setText(text, true);
        mailSender.send(message);
    }

    public void sendProviderVerificationEmail(String toEmail, String name, String subject, String  token) throws MessagingException {

        Context context = new Context();
        context.setVariables(Map.of("name",name , "verificationUrl", getProviderVerificationUrl(token)));
        String text = templateEngine.process("verificationEmail", context);
        MimeMessage message = getMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setPriority(1);
        helper.setSubject(subject);
        helper.setFrom("nikhilbudhathoki0@gmail.com");
        helper.setTo(toEmail);
        helper.setText(text, true);
        mailSender.send(message);
    }

    public void sendUserPasswordResetEmail(String toEmail, String name, String subject, String token) throws MessagingException {
        String resetUrl = getUserPasswordResetUrl(token);
        Context context = new Context();
        context.setVariables(Map.of("name", name, "resetUrl", resetUrl));
        String text = templateEngine.process("passwordResetMail", context); // Assume you have a template named passwordResetEmailUser.html
        MimeMessage message = getMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setPriority(1);
        helper.setSubject(subject);
        helper.setFrom("nikhilbudhathoki0@gmail.com");
        helper.setTo(toEmail);
        helper.setText(text, true);
        mailSender.send(message);
    }

    public void sendProviderPasswordResetEmail(String toEmail, String name, String subject, String token) throws MessagingException {
        String resetUrl = getProviderPasswordResetUrl(token);
        Context context = new Context();
        context.setVariables(Map.of("name", name, "resetUrl", resetUrl));
        String text = templateEngine.process("passwordResetMail", context); // Assume you have a template named passwordResetEmailUser.html
        MimeMessage message = getMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setPriority(1);
        helper.setSubject(subject);
        helper.setFrom("nikhilbudhathoki0@gmail.com");
        helper.setTo(toEmail);
        helper.setText(text, true);
        mailSender.send(message);
    }

    public void sendStatusChangeEmail(String toEmail, String name, Status status, String subject) throws MessagingException {
        Context context = new Context();
        context.setVariables(Map.of(
                "name", name,
                "status", status.toString().toLowerCase() // Using the status for conditional rendering in the template
        ));
        String text = templateEngine.process("statusChangeEmail", context);

        MimeMessage message = getMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setPriority(1);
        helper.setSubject(subject);
        helper.setFrom("nikhilbudhathoki0@gmail.com");
        helper.setTo(toEmail);
        helper.setText(text, true);
        mailSender.send(message);
    }



    private String getUserPasswordResetUrl(String token) {
        return "http://localhost:8000/api/v1/auth/users/reset-password?token=" + token;
    }

    private String getProviderPasswordResetUrl(String token) {
        return "http://localhost:8000/api/v1/auth/providers/reset-password?token=" + token;
    }



    public String getVerificationUrl(String token){
        String url = "http://localhost:8000/api/v1/auth/verify?token=" + token;
        return url;
    }

    public String getProviderVerificationUrl(String token){
        String url = "http://localhost:8000/api/v1/auth/providers/verify?token=" + token;
        return url;
    }


}
