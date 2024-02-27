package homiessecurity.entities;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

public class  CompositeDetailsService implements UserDetailsService {

    private final List<UserDetailsService> services;

    public CompositeDetailsService(List<UserDetailsService> services) {
        this.services = services;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        for (UserDetailsService delegate : this.services) {
            try {
                UserDetails userDetails = delegate.loadUserByUsername(username);
                if (userDetails != null) {
                    return userDetails;
                }
            } catch (UsernameNotFoundException ignored) {
                // User not found in this service, continue to the next one
            }
        }
        throw new UsernameNotFoundException("User not found: " + username);
    }
}

