package spring.gr.socioai.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import spring.gr.socioai.service.AuthenticationService;
import spring.gr.socioai.controller.http.requests.LoginRequest;
import spring.gr.socioai.controller.http.requests.RegisterRequest;
import spring.gr.socioai.controller.http.responses.TokenResponse;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticatedUserController {

    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody @Validated LoginRequest req) {
        return ResponseEntity.ok().body(new TokenResponse(authenticationService.login(req)));
    }

    @PostMapping("/register")
    public ResponseEntity<TokenResponse> singUp(@RequestBody @Validated RegisterRequest req) {
        return ResponseEntity.ok().body(new TokenResponse(authenticationService.register(req)));
    }
}
