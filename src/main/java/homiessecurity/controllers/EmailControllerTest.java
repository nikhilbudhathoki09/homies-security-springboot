package homiessecurity.controllers;

import homiessecurity.dtos.EmailVerification.EmailVerificationResponse;
import homiessecurity.entities.EmailVerification;
import homiessecurity.service.EmailVerificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/auth")
@RequiredArgsConstructor
public class EmailControllerTest {

    private final  EmailVerificationService emailService;




}
