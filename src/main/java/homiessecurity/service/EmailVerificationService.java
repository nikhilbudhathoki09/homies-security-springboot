package homiessecurity.service;

import homiessecurity.dtos.EmailVerification.EmailVerificationResponse;
import homiessecurity.entities.EmailVerification;
import homiessecurity.entities.User;
import org.springframework.stereotype.Service;

@Service
public interface EmailVerificationService {

    public EmailVerificationResponse getEmailVerification(User user);

    public EmailVerification validateOtp(String verificationToken,String otp);

    public EmailVerification getEmailVerificationByToken(String token);

    public String confirmToken(String token);

}
