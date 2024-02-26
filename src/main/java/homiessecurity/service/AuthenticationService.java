package homiessecurity.service;

import homiessecurity.dtos.Auth.AuthenticationResponse;
import homiessecurity.dtos.Auth.LoginRequest;
import homiessecurity.dtos.EmailVerification.EmailVerificationResponse;
import homiessecurity.dtos.Users.UserDto;
import homiessecurity.dtos.Users.UserRegisterDto;
import homiessecurity.entities.Role;
import homiessecurity.entities.User;
import homiessecurity.exceptions.CustomAuthenticationException;
import homiessecurity.exceptions.ResourceAlreadyExistsException;
import homiessecurity.exceptions.ResourceNotFoundException;
import homiessecurity.payload.ApiResponse;
import homiessecurity.repository.RoleRepository;
import homiessecurity.repository.UserRepository;
import homiessecurity.security.JwtService;
import homiessecurity.service.impl.UserServiceImpl;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final ModelMapper modelMapper;
    private final UserServiceImpl userService;
    private final UserRepository userRepo;
    private final RoleRepository roleRepo;
    private final EmailVerificationService emailVerificationService;
    private final EmailSenderService emailSenderService;
    private final CloudinaryService cloudinary;


    public ApiResponse registerUser(UserRegisterDto request)  {

        if(userRepo.existsByEmail(request.getEmail()))
            throw new ResourceAlreadyExistsException("Email already exists. Try a new one");
        if(userRepo.existsByPhoneNumber(request.getPhoneNumber())){
            throw new ResourceAlreadyExistsException("PhoneNumber is already  in use. Try a new one ");
        }

        //searching the role from the table if not found creating a role
        Role userRole = roleRepo.findByTitle("ROLE_USER")
                .orElseGet(() -> roleRepo.save(Role.builder().title("ROLE_USER").description("Normal User").build()));

        //uploading the image to cloudinary
        String imageUrl = cloudinary.uploadImage(request.getUserImage(),"UserProfile");

        var user = User.builder()
                .name(request.getUsername())
                .email(request.getEmail())
                .email(request.getEmail())
                .gender(request.getGender())
                .userImage(imageUrl)
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

        return new ApiResponse("User Registered. Please verify your email to proceed.", true);
    }

    public AuthenticationResponse authenticateUser(LoginRequest request) {
        try {
            if (!userRepo.existsByEmail(request.getEmail())) {
                throw new ResourceNotFoundException("User", "email", request.getEmail());
            }

            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword())
            );

            var user = userService.loadUserByUsername(request.getEmail());
            User newUser = userService.getUserByEmail(request.getEmail());

            if (!newUser.isVerified()) {
                throw new CustomAuthenticationException("User is not verified. Please verify your email.");
            }

            var jwtToken = jwtService.generateToken(user);
            return AuthenticationResponse.builder()
                    .accessToken(jwtToken)
                    .user(modelMapper.map(newUser, UserDto.class))
                    .build();
        } catch (BadCredentialsException e) {
            // Handle bad credentials exception and throw a custom exception with a meaningful message
            throw new CustomAuthenticationException("Invalid credentials. Please check your email and password.");
        }
    }




}
