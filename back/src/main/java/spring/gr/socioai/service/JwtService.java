package spring.gr.socioai.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import spring.gr.socioai.model.valueobjects.Email;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

@Service
public class JwtService {

    @Value("${app.security.jwt-secret}")
    private String SECRET_KEY;

    public String validateToken(String token) {
        return JWT.require(Algorithm.HMAC256(SECRET_KEY))
                .withIssuer("app-auth-api")
                .build()
                .verify(token)
                .getSubject();
    }

    public String createToken(Email email) {
        return JWT.create()
                .withIssuer("app-auth-api")
                .withSubject(email.getEmail())
                .withIssuedAt(new Date())
                .withExpiresAt(genExpirationDateMinutes(30))
                .sign(Algorithm.HMAC256(SECRET_KEY));
    }

    private Instant genExpirationDateMinutes(int minutes){
        return LocalDateTime.now().plusMinutes(minutes).toInstant(ZoneOffset.of("-03:00"));
    }

//    private Instant genExpirationDateHours(int hours){
//        return LocalDateTime.now().plusMinutes(hours).toInstant(ZoneOffset.of("-03:00"));
//    }
}
