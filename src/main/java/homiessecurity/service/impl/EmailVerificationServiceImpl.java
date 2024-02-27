package homiessecurity.service.impl;


import homiessecurity.dtos.EmailVerification.EmailVerificationResponse;
import homiessecurity.entities.EmailVerification;
import homiessecurity.entities.ServiceProvider;
import homiessecurity.entities.User;
import homiessecurity.payload.ApiResponse;
import homiessecurity.repository.EmailVerificationRepository;
import homiessecurity.service.EmailVerificationService;
import homiessecurity.service.ProviderService;
import homiessecurity.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;


@Service
public class EmailVerificationServiceImpl implements EmailVerificationService {

    private final EmailVerificationRepository emailVerificationRepo;
    private final PasswordEncoder encoder;
    private final UserService userService;
    private final ProviderService providerService;

    public EmailVerificationServiceImpl(EmailVerificationRepository emailVerificationRepo, PasswordEncoder encoder,
                                        UserService userService, ProviderService providerService) {
        this.emailVerificationRepo = emailVerificationRepo;
        this.encoder = encoder;
        this.userService = userService;
        this.providerService = providerService;
    }

    @Override
    public EmailVerificationResponse getEmailVerification(User user) {
        Random random = new Random();
        String otp = String.valueOf(random.nextInt(99999));

        String verificationToken = String.valueOf(UUID.randomUUID());

        EmailVerification emailVerification = EmailVerification.builder()
                .otp(encoder.encode(otp))
                .sentAt(LocalDateTime.now())
                .verificationToken(verificationToken)
                .user(user)
                .build();

        emailVerificationRepo.save(emailVerification); //saving the otp and verification token in the database
        return EmailVerificationResponse.builder()
                .otp(otp)
                .verificationToken(verificationToken)
                .build();
    }

    @Override
    public EmailVerificationResponse getEmailVerification(ServiceProvider provider) {
        Random random = new Random();
        String otp = String.valueOf(random.nextInt(99999));

        String verificationToken = String.valueOf(UUID.randomUUID());

        EmailVerification emailVerification = EmailVerification.builder()
                .otp(encoder.encode(otp))
                .sentAt(LocalDateTime.now())
                .verificationToken(verificationToken)
                .provider(provider)
                .build();

        emailVerificationRepo.save(emailVerification); //saving the otp and verification token in the database
        return EmailVerificationResponse.builder()
                .otp(otp)
                .verificationToken(verificationToken)
                .build();
    }

    @Override
    public EmailVerification validateOtp(String verificationToken, String otp) {
        EmailVerification emailVerification = emailVerificationRepo.findByVerificationToken(verificationToken)
                .orElseThrow(() -> new IllegalStateException("Invalid verification token"));

        if (!encoder.matches(otp, emailVerification.getOtp())) {
            throw new IllegalStateException("Invalid OTP");
        }

        if (emailVerification.getUser().isVerified() || emailVerification.getVerifiedAt() != null) {
            throw new IllegalStateException("Email is already verified");
        }

        if (LocalDateTime.now().isAfter(emailVerification.getSentAt().plusMinutes(5))) {
            throw new IllegalStateException("OTP expired");
        }

        emailVerification.setVerifiedAt(LocalDateTime.now());
        userService.verifyUser(emailVerification.getUser().getEmail()); //verifyihg the user according to the token
        return emailVerificationRepo.save(emailVerification);
    }

    public  EmailVerification getEmailVerificationByToken(String token){
        return emailVerificationRepo.findByVerificationToken(token)
                .orElseThrow(() -> new IllegalStateException("Invalid verification token"));
    }

    @Transactional
    public String confirmToken(String token) {

        EmailVerification confirmToken = emailVerificationRepo.findByVerificationToken(token)
                .orElseThrow(() -> new IllegalStateException("Invalid verification token"));

        if (confirmToken.getVerifiedAt() != null) {
            throw new IllegalStateException("Email is already confirmed");
        }
        if (LocalDateTime.now().isAfter(confirmToken.getSentAt().plusMinutes(5))) {
            throw new IllegalStateException("Token is already expired");
        }

        confirmToken.setVerifiedAt(LocalDateTime.now());
        userService.verifyUser(confirmToken.getUser().getEmail()); //verifyihg the user according to the token

        //Returning confirmation message if the token matches
        return "Your email is confirmed. Thank you for using our service!";
    }

    @Transactional
    public ApiResponse confirmProviderToken(String token){
        EmailVerification confirmToken = emailVerificationRepo.findByVerificationToken(token)
                .orElseThrow(() -> new IllegalStateException("Invalid verification token"));

        if (confirmToken.getVerifiedAt() != null) {
            throw new IllegalStateException("Email is already confirmed");
        }
        if (LocalDateTime.now().isAfter(confirmToken.getSentAt().plusMinutes(5))) {
            throw new IllegalStateException("Token is already expired");
        }

        confirmToken.setVerifiedAt(LocalDateTime.now());
        providerService.verifyProvider(confirmToken.getProvider().getEmail()); //verifying the email of the provider

        return new ApiResponse("Your email is confirmed. Thank you for using our service!", true);
    }
}
