package homiessecurity.service;

import jakarta.mail.MessagingException;

public interface EmailSenderService {

    public void sendHtmlEmail(String toEmail,String name, String subject) throws MessagingException;

    public void sendVerificationEmail(String toEmail, String name, String subject, String  token) throws MessagingException;

    public void sendProviderVerificationEmail(String toEmail, String name, String subject, String  token) throws MessagingException;

    public void sendUserPasswordResetEmail(String toEmail, String name, String subject, String token) throws MessagingException;

    public void sendProviderPasswordResetEmail(String toEmail, String name, String subject, String token) throws MessagingException;

    }
