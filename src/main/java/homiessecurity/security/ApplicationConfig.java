package homiessecurity.security;

import homiessecurity.service.UserService;
import homiessecurity.service.impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {

    private final UserServiceImpl serviceImpl;


    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(serviceImpl);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    public
}
