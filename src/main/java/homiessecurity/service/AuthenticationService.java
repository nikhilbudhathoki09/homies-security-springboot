package homiessecurity.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import homiessecurity.dtos.Auth.AuthenticationResponse;
import homiessecurity.dtos.Auth.LoginRequest;
import homiessecurity.dtos.EmailVerification.EmailVerificationResponse;
import homiessecurity.dtos.UserRegisterDto;
import homiessecurity.entities.RefreshToken;
import homiessecurity.entities.Role;
import homiessecurity.entities.User;
import homiessecurity.exceptions.ResourceAlreadyExistsException;
import homiessecurity.exceptions.ResourceNotFoundException;
import homiessecurity.repository.RefreshTokenRepository;
import homiessecurity.repository.RoleRepository;
import homiessecurity.repository.UserRepository;
import homiessecurity.security.JwtService;
import homiessecurity.service.impl.UserServiceImpl;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserServiceImpl userService;
    private final UserRepository userRepo;
    private final RoleRepository roleRepo;
    private final RefreshTokenRepository tokenRepo;
    private final EmailVerificationService emailVerificationService;
    private final EmailSenderService emailSenderService;


    public AuthenticationResponse register(UserRegisterDto request)  {

        if(userRepo.existsByEmail(request.getEmail()))
            throw new ResourceAlreadyExistsException("Email already exists. Try a new one");
        if(userRepo.existsByPhoneNumber(request.getPhoneNumber())){
            throw new ResourceAlreadyExistsException("PhoneNumber is already  in use. Try a new one ");
        }

        //searching the role from the table if not found creating a role
        Role userRole = roleRepo.findByTitle("USER")
                .orElseGet(() -> roleRepo.save(Role.builder().title("USER").description("Normal User").build()));

        var user = User.builder()
                .name(request.getUsername())
                .email(request.getEmail())
                .email(request.getEmail())
                .gender(request.getGender())
                .phoneNumber(request.getPhoneNumber())
                .address(request.getAddress())
                .isVerified(false)
                .password(passwordEncoder.encode(request.getPassword()))
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .userRoles(Set.of(userRole))
                .build();

        User savedUser = userRepo.save(user);
        //creating a new jwt token for the user
        var jwtToken = jwtService.generateToken(savedUser);

        //verifying the user email which returns(otp and verification token)
        EmailVerificationResponse emailResponse = emailVerificationService.getEmailVerification(savedUser);

        try {
            //sending the email to the user with verification link to get verified
            emailSenderService.sendVerificationEmail(savedUser.getEmail(),
                    savedUser.getName(),
                    "Verify your email",
                    emailResponse.getVerificationToken());

            System.out.println("username : " + savedUser.getName());
        }catch (MessagingException e){
            System.out.println("Error sending email");
            throw new RuntimeException(e);
        }

        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .build();
    }

    public AuthenticationResponse authenticate(LoginRequest request){

        if(!userRepo.existsByEmail(request.getEmail())){
            throw new ResourceNotFoundException("User" , "email", request.getEmail());}

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword())
        );

        var user = userService.loadUserByUsername(request.getEmail());

        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .build();

    }



}
