package homiessecurity.service;

import homiessecurity.entities.Appointment;
import homiessecurity.entities.Status;
import jakarta.mail.MessagingException;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface EmailSenderService {

    public void sendHtmlEmail(String toEmail,String name,
                              String subject) throws MessagingException;

    public void sendVerificationEmail(String toEmail, String name, String subject,
                                      String  token) throws MessagingException;

    public void sendProviderVerificationEmail(String toEmail, String name, String subject,
                                              String  token) throws MessagingException;

    public void sendUserPasswordResetEmail(String toEmail, String name, String subject,
                                           String token) throws MessagingException;

    public void sendProviderPasswordResetEmail(String toEmail, String name,
                                               String subject, String token) throws MessagingException;

    public void sendStatusChangeEmail(String toEmail, String name,
                                      Status status, String subject) throws MessagingException;

    public void sendAppointmentStatusEmail(String toEmail, String userName, String providerName,
                                           Status status, LocalDate appointmentDate, String appointmentTime);

    public void sendReminderEmail(Appointment appointment);

    public void sendNewRequestEmail(Appointment appointment);




}
