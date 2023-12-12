package homiessecurity.service.impl;

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
    public void sendEmail(String toEmail, String body, String subject) throws MessagingException {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("nikhilbudhathoki0@gmail.com");
        message.setTo(toEmail);
        message.setText(body);
        message.setSubject(subject);

        mailSender.send(message);

        System.out.println("Mail sent sucessfully");

    }

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
        System.out.println("Mail sent sucessfully");
    }

    public String getVerificationUrl(String token){
        return "http://localhost:3232/api/v1/auth/verify?token=" + token;
    }


}
