package spring.gr.socioai.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.password.CompromisedPasswordChecker;
import org.springframework.security.authentication.password.CompromisedPasswordException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import spring.gr.socioai.model.AuthenticatedUserEntity;
import spring.gr.socioai.model.valueobjects.Email;
import spring.gr.socioai.repository.AuthenticatedUserRepository;
import spring.gr.socioai.controller.http.requests.LoginRequest;
import spring.gr.socioai.controller.http.requests.RegisterRequest;
import spring.gr.socioai.repository.AuthenticatedUserRoleRepository;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final AuthenticatedUserRepository authenticatedUserRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final CompromisedPasswordChecker compromisedPasswordChecker;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticatedUserRoleRepository roleRepository;

    public String login(LoginRequest request) {
        var user = new UsernamePasswordAuthenticationToken(new Email(request.email()), request.password());
        var authentication = authenticationManager.authenticate(user);

        var authUser = (AuthenticatedUserEntity) authentication.getPrincipal();

        return jwtService.createToken(new Email(authUser.getUsername()));
    }

    public String register(RegisterRequest req) {

        if(compromisedPasswordChecker.check(req.password()).isCompromised()) {
            throw new CompromisedPasswordException("Essa senha é insegura! Por favor, escolha uma outra.");
        }

        var user = new AuthenticatedUserEntity(
                null,
                new Email(req.email()),
                passwordEncoder.encode(req.password()),
                roleRepository.getReferenceById(1L), // salva como user básico
                null
        );

        this.authenticatedUserRepository.save(user);
        return jwtService.createToken(new Email(user.getUsername()));
    }
}
