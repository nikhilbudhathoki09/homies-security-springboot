package homiessecurity.controllers;

import homiessecurity.dtos.Auth.AuthenticationResponse;
import homiessecurity.dtos.Auth.LoginRequest;
import homiessecurity.dtos.Users.UserRegisterDto;
import homiessecurity.payload.ApiResponse;
import homiessecurity.service.AuthenticationService;
import homiessecurity.service.EmailVerificationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")

public class authController {

    private final AuthenticationService authService;
    private final EmailVerificationService emailService;

    public authController(AuthenticationService authService, EmailVerificationService emailService) {
        this.authService = authService;
        this.emailService = emailService;
    }


    @PostMapping("/register")
    public ResponseEntity<ApiResponse> registerUser(@Valid @RequestBody UserRegisterDto register ){
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




}
