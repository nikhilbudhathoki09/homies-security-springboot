package homiessecurity.service.impl;

import homiessecurity.entities.Appointment;
import homiessecurity.entities.Status;
import homiessecurity.exceptions.CustomCommonException;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
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
//        return "http://localhost:8000/api/v1/auth/users/reset-password?token=" + token;
       return "http://localhost:5173/reset-password?token=" + token;
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

    public void sendAppointmentStatusEmail(String toEmail, String userName, String providerName, Status status, LocalDate appointmentDate, String appointmentTime) {
        try{
            Context context = new Context();
            context.setVariable("name", userName);
            context.setVariable("providerName", providerName);
            context.setVariable("status", status.toString().toLowerCase()); // "accepted" or "rejected"
            if (status == Status.ACCEPTED) {
                context.setVariable("appointmentDate", appointmentDate.toString());
                context.setVariable("appointmentTime", appointmentTime); // Include appointment time
            }
            String text = templateEngine.process("appointmentStatusEmail", context); // Use your email template name here

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setSubject("Appointment Status Update");
            helper.setFrom("nikhilbudhathoki0@gmail.com"); // Specify your email sender
            helper.setTo(toEmail);
            helper.setText(text, true);
            mailSender.send(message);
        }catch(MessagingException e){
            throw new CustomCommonException(e.getMessage());
        }
    }

    public void sendReminderEmail(Appointment appointment) {
        try {
            Context context = new Context();
            context.setVariable("providerName", appointment.getProvider().getProviderName());
            context.setVariable("clientName", appointment.getUser().getName());
            context.setVariable("appointmentTime", appointment.getArrivalTime());
            context.setVariable("serviceName", appointment.getService().getServiceName());
            context.setVariable("detailedLocation", appointment.getDetailedLocation());
            context.setVariable("arrivalTime", appointment.getArrivalTime());

            String text = templateEngine.process("ReminderEmail", context);

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setSubject("🗓️ Appointment Reminder 🗓️");
            String fromEmail = "nikhilbudhathoki0@gmail.com";
            fromEmail = fromEmail.trim(); // Ensure there's no leading or trailing whitespace
            helper.setFrom(fromEmail);
            helper.setTo(appointment.getProvider().getEmail());
            helper.setText(text, true);
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new CustomCommonException(e.getMessage());
        }
    }

    public void sendNewRequestEmail(Appointment appointment) {
        try {
            Context context = new Context();
            context.setVariable("providerName", appointment.getProvider().getProviderName());
            context.setVariable("clientName", appointment.getUser().getName());
            context.setVariable("appointmentTime", appointment.getArrivalTime());
            context.setVariable("serviceName", appointment.getService().getServiceName());
            context.setVariable("detailedLocation", appointment.getDetailedLocation());
            context.setVariable("arrivalTime", appointment.getArrivalTime());

            String text = templateEngine.process("request", context);

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setSubject("🗓️ New Appointment Request 🗓️");
            String fromEmail = "nikhilbudhathoki0@gmail.com";
            fromEmail = fromEmail.trim(); // Ensure there's no leading or trailing whitespace
            helper.setFrom(fromEmail);
            helper.setTo(appointment.getProvider().getEmail());
            helper.setText(text, true);

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new CustomCommonException(e.getMessage());
        }
    }







}
