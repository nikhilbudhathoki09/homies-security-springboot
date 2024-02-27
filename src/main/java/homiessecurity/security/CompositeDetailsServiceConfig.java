package homiessecurity.security;

import homiessecurity.entities.CompositeDetailsService;
import homiessecurity.service.impl.ProviderServiceImpl;
import homiessecurity.service.impl.UserServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class CompositeDetailsServiceConfig {

    private final UserServiceImpl userServiceImpl;
    private final ProviderServiceImpl providerServiceImpl;

    public CompositeDetailsServiceConfig(UserServiceImpl userServiceImpl, ProviderServiceImpl providerServiceImpl) {
        this.userServiceImpl = userServiceImpl;
        this.providerServiceImpl = providerServiceImpl;
    }

    @Bean
    public CompositeDetailsService compositeDetailsService() {
        return new CompositeDetailsService(List.of(userServiceImpl, providerServiceImpl));
    }
}

