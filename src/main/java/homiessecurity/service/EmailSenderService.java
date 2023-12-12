package homiessecurity.service;

import jakarta.mail.MessagingException;

public interface EmailSenderService {

    public void sendEmail(String toEmail, String body, String subject) throws MessagingException;

    public void sendHtmlEmail(String toEmail,String name, String subject) throws MessagingException;

    public void sendVerificationEmail(String toEmail, String name, String subject, String  token) throws MessagingException;
}
