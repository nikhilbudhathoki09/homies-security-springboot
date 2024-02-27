package homiessecurity.security;

import homiessecurity.entities.CompositeDetailsService;
import homiessecurity.service.UserService;
import homiessecurity.service.impl.ProviderServiceImpl;
import homiessecurity.service.impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;

//@Configuration
//@RequiredArgsConstructor
//public class ApplicationConfig {
//
//    private final UserServiceImpl userServiceImpl;
//    private final ProviderServiceImpl providerServiceImpl;
//    private final CompositeDetailsService compositeDetailsService;
//
//
//    @Bean
//    public AuthenticationProvider authenticationProvider(){
//        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
//        authProvider.setUserDetailsService(compositeDetailsService);
//        authProvider.setPasswordEncoder(passwordEncoder());
//
//        return authProvider;
//    }
//
//    @Bean
//    public PasswordEncoder passwordEncoder(){
//        return new BCryptPasswordEncoder();
//    }
//
//    @Bean
//    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception{
//        return configuration.getAuthenticationManager();
//    }
//
//    @Bean
//    public UserServiceImpl userService() {
//        return userServiceImpl;
//    }
//
//    @Bean
//    public ProviderServiceImpl providerService() {
//        return providerServiceImpl;
//    }
//
//    @Bean
//    public CompositeDetailsService compositeDetailsService() {
//        return new CompositeDetailsService(List.of(userService(), providerService()));
//    }

@Configuration
public class ApplicationConfig {

    private final UserServiceImpl userServiceImpl;
    private final ProviderServiceImpl providerServiceImpl;
    private final CompositeDetailsService compositeDetailsService;

    private final PasswordEncoder encoder;

    @Autowired
    public ApplicationConfig(UserServiceImpl userServiceImpl, ProviderServiceImpl providerServiceImpl
            , CompositeDetailsService compositeDetailsService , PasswordEncoder encoder) {
        this.userServiceImpl = userServiceImpl;
        this.providerServiceImpl = providerServiceImpl;
        this.compositeDetailsService = compositeDetailsService;
        this.encoder = encoder;
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(compositeDetailsService);
        authProvider.setPasswordEncoder(encoder);
        return authProvider;
    }



    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }


}



