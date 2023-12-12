package homiessecurity.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import homiessecurity.dtos.Auth.AuthenticationResponse;
import homiessecurity.dtos.Auth.LoginRequest;
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
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Instant;
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


    public AuthenticationResponse register(UserRegisterDto request){

        if(userRepo.existsByEmail(request.getEmail()))
            throw new ResourceAlreadyExistsException("Email already exists. Try a new one");
        if(userRepo.existsByPhoneNumber(request.getPhoneNumber())){
            throw new ResourceAlreadyExistsException("PhoneNumber is already  in use. Try a new one ");
        }

        //searching the role from the table if not found creating a role
        Role userRole = roleRepo.findByTitle("USER")
                .orElseGet(() -> roleRepo.save(Role.builder().title("USER").description("Normal User").build()));

        var user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .email(request.getEmail())
                .gender(request.getGender())
                .phoneNumber(request.getPhoneNumber())
                .address(request.getAddress())
                .password(passwordEncoder.encode(request.getPassword()))
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .userRoles(Set.of(userRole))
                .build();

        User savedUser = userRepo.save(user);
        var jwtToken = jwtService.generateToken(savedUser);
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

    public void saveRefreshToken(User user, String refreshToken){
        var token = RefreshToken.builder()
                .user(user)
                .token(refreshToken)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepo.save(token);
    }

    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    )throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String userEmail;

        if(authHeader == null || !authHeader.startsWith("Bearer ")){
            return;
        }

        refreshToken = authHeader.substring(7);
        userEmail = jwtService.extractUsername(refreshToken);
        if(userEmail != null){
            var user = userService.loadUserByUsername(userEmail);
            if(jwtService.isTokenValid(refreshToken, user)){
                var accessToken = jwtService.generateToken(user);
                var authResponse = AuthenticationResponse.builder()
                        .accessToken(accessToken)
                        .build();

                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
        }
    }
}
