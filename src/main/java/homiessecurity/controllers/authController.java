package homiessecurity.controllers;

import homiessecurity.dtos.Auth.AuthenticationResponse;
import homiessecurity.dtos.Auth.LoginRequest;
import homiessecurity.dtos.UserRegisterDto;
import homiessecurity.entities.EmailVerification;
import homiessecurity.service.AuthenticationService;
import homiessecurity.service.EmailVerificationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")

public class authController {

    private final AuthenticationService authService;
    private final EmailVerificationService emailService;

    public authController(AuthenticationService authService, EmailVerificationService emailService){
        this.authService = authService;
        this.emailService = emailService;
    }


    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> registerUser(@RequestBody UserRegisterDto register ){
        AuthenticationResponse response = this.authService.register(register);
        return new ResponseEntity<AuthenticationResponse>(response, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody LoginRequest request){
        AuthenticationResponse response = this.authService.authenticate(request);
        return new ResponseEntity<AuthenticationResponse>(response, HttpStatus.OK);
    }

    @PostMapping(value = "/verify")
    public ResponseEntity<?>verifyUser(@RequestParam String token){
        EmailVerification emailVerification = emailService.getEmailVerificationByToken(token);
        System.out.println(emailVerification);
        return ResponseEntity.ok(emailVerification);
    }


}
