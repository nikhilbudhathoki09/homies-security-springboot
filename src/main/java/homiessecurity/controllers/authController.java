package homiessecurity.controllers;

import homiessecurity.dtos.Auth.AuthenticationResponse;
import homiessecurity.dtos.Auth.LoginRequest;
import homiessecurity.dtos.UserRegisterDto;
import homiessecurity.service.AuthenticationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")

public class authController {

    private final AuthenticationService authService;

    public authController(AuthenticationService authService){
        this.authService = authService;
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


}
