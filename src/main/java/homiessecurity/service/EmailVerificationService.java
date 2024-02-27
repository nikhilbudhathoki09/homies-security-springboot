package homiessecurity.service;

import homiessecurity.dtos.EmailVerification.EmailVerificationResponse;
import homiessecurity.entities.EmailVerification;
import homiessecurity.entities.ServiceProvider;
import homiessecurity.entities.User;
import homiessecurity.payload.ApiResponse;
import org.springframework.stereotype.Service;


public interface EmailVerificationService {

    public EmailVerificationResponse getEmailVerification(User user);

    public EmailVerificationResponse getEmailVerification(ServiceProvider provider);

    public EmailVerification validateOtp(String verificationToken,String otp);

    public EmailVerification getEmailVerificationByToken(String token);

    public String confirmToken(String token);

    public ApiResponse confirmProviderToken(String token);

}
