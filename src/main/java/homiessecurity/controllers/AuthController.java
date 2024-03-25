package homiessecurity.controllers;

import homiessecurity.dtos.Auth.AuthenticationResponse;
import homiessecurity.dtos.Auth.LoginRequest;
import homiessecurity.dtos.Auth.PasswordResetRequest;
import homiessecurity.dtos.Providers.ProviderDto;
import homiessecurity.dtos.Providers.ProviderRegistrationRequestDto;
import homiessecurity.dtos.Users.UserRegisterDto;
import homiessecurity.exceptions.CustomCommonException;
import homiessecurity.payload.ApiResponse;
import homiessecurity.service.AuthenticationService;
import homiessecurity.service.EmailVerificationService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.security.Provider;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/v1/auth")

public class AuthController {

    private final AuthenticationService authService;
    private final EmailVerificationService emailService;

    public AuthController(AuthenticationService authService, EmailVerificationService emailService) {
        this.authService = authService;
        this.emailService = emailService;
    }


    @PostMapping("/register")
    public ResponseEntity<ApiResponse> registerUser(@Valid @ModelAttribute UserRegisterDto register,
                                                    @RequestParam(value = "userImage", required = false) MultipartFile file){
        register.setUserImage(file);
        ApiResponse response = this.authService.registerUser(register);
        return new ResponseEntity<ApiResponse>(response, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@Valid@RequestBody LoginRequest request){
        AuthenticationResponse response = this.authService.authenticateUser(request);
        return new ResponseEntity<AuthenticationResponse>(response, HttpStatus.OK);
    }

    @GetMapping (value = "/verify")
    public ResponseEntity<String>verifyUser(@RequestParam String token){
        return new ResponseEntity<String>(emailService.confirmToken(token), HttpStatus.OK);
    }

    @PostMapping("/providers/login")
    public ResponseEntity<AuthenticationResponse> providerLogin(@Valid@RequestBody LoginRequest request){
        AuthenticationResponse response = this.authService.authenticateProvider(request);
        return new ResponseEntity<AuthenticationResponse>(response, HttpStatus.OK);
    }

    @PostMapping("/providers/register")
    public ResponseEntity<ProviderDto> registerProvider(@Valid ProviderRegistrationRequestDto register,
                                                        @RequestParam(value = "providerImage", required = false) MultipartFile providerImage,
                                                        @RequestParam(value="registrationDocument") MultipartFile registrationDocument,
                                                        @RequestParam(value="experienceDocument") MultipartFile experienceDocument) {

        if (registrationDocument.isEmpty()) {
            throw new CustomCommonException("Registration document must not be empty.");
        }

        if (experienceDocument.isEmpty()) {
            throw new CustomCommonException("Registration document must not be empty.");
        }
        if (providerImage != null && !providerImage.isEmpty()) {
            register.setProviderImage(providerImage);
        } else {
            register.setProviderImage(null);
        }

        register.setRegistrationDocument(registrationDocument);
        register.setExperienceDocument(experienceDocument);

        ProviderDto provider = this.authService.registerProvider(register);
        return new ResponseEntity<>(provider, HttpStatus.CREATED);
    }


    @GetMapping(value="/providers/verify")
    public ResponseEntity<ApiResponse> verifyProvider(@RequestParam String token){
        return new ResponseEntity<ApiResponse>(emailService.confirmProviderToken(token), HttpStatus.OK);
    }

// Endpoint to initiate a password reset request for a User
    @PostMapping("/user/reset-password-request")
    public ResponseEntity<?> requestUserPasswordReset(@RequestBody PasswordResetRequest passwordResetRequest) throws MessagingException {
        emailService.initiatePasswordResetForUser(passwordResetRequest.getEmail());
        return ResponseEntity.ok("Password reset link has been sent to your email.");
    }

    // Endpoint to reset a User's password
    @PostMapping("/user/reset-password")
    public ResponseEntity<?> resetUserPassword(@RequestParam String token, @RequestBody PasswordResetRequest passwordResetRequest) {
        emailService.resetUserPassword(token, passwordResetRequest.getNewPassword());
        return ResponseEntity.ok("Your password has been successfully reset.");
    }

    // Endpoint to initiate a password reset request for a ServiceProvider
    @PostMapping("/provider/reset-password-request")
    public ResponseEntity<?> requestProviderPasswordReset(@RequestBody PasswordResetRequest passwordResetRequest)throws MessagingException {
        emailService.initiatePasswordResetForProvider(passwordResetRequest.getEmail());
        return ResponseEntity.ok("Password reset link has been sent to your email.");
    }


    @PostMapping("/provider/reset-password")
    public ResponseEntity<?> resetProviderPassword(@RequestParam String token, @RequestBody PasswordResetRequest passwordResetRequest) {
        emailService.resetProviderPassword(token, passwordResetRequest.getNewPassword());
        return ResponseEntity.ok("Your password has been successfully reset.");
    }

}
