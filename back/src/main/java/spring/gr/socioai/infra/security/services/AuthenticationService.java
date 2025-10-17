package spring.gr.socioai.infra.security.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.password.CompromisedPasswordChecker;
import org.springframework.security.authentication.password.CompromisedPasswordException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import spring.gr.socioai.infra.security.model.AuthenticatedUserEntity;
import spring.gr.socioai.infra.security.model.Email;
import spring.gr.socioai.infra.security.repository.AuthenticatedUserRepository;
import spring.gr.socioai.interfaces.http.requests.LoginRequest;
import spring.gr.socioai.interfaces.http.requests.RegisterRequest;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final AuthenticatedUserRepository authenticatedUserRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final CompromisedPasswordChecker compromisedPasswordChecker;
    private final PasswordEncoder passwordEncoder;

    public String login(LoginRequest request) {
        var user = new UsernamePasswordAuthenticationToken(request.email(), request.password());
        var authentication = authenticationManager.authenticate(user);

        var authUser = (AuthenticatedUserEntity) authentication.getPrincipal();

        return jwtService.createToken(new Email(authUser.getUsername()));
    }

    public String register(RegisterRequest req) {

        if(compromisedPasswordChecker.check(req.password()).isCompromised()) {
            throw new CompromisedPasswordException("Essa senha é insegura! Por favor, escolha uma outra.");
        }

        var user = new AuthenticatedUserEntity(
                req.email(),
                passwordEncoder.encode(req.password()));

        this.authenticatedUserRepository.save(user);
        return jwtService.createToken(new Email(user.getUsername()));
    }
}
