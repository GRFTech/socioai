package spring.gr.socioai.service;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import spring.gr.socioai.model.valueobjects.Email;
import spring.gr.socioai.repository.AuthenticatedUserRepository;

@Configuration
public class UserDetailsServiceImpl implements UserDetailsService {

    private final AuthenticatedUserRepository authenticatedUserRepository;

    public UserDetailsServiceImpl(AuthenticatedUserRepository authenticatedUserRepository) {
        this.authenticatedUserRepository = authenticatedUserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return authenticatedUserRepository.findByUsername(new Email(username))
                .orElseThrow(() -> new UsernameNotFoundException("User not found!"));
    }
}
