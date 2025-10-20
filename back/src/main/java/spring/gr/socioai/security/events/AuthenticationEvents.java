package spring.gr.socioai.security.events;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AuthenticationEvents {

    @EventListener
    public void onSuccess(AuthenticationSuccessEvent event) {
        log.info("Authentication successful for user: {}", event.getAuthentication().getName());

        // Logica para enviar um email de verificação ou de bem vindo, por exemplo
    }

    @EventListener
    public void onFailure(AbstractAuthenticationFailureEvent event) {
        log.info("Authentication failed for user: {}", event.getAuthentication().getName());

        // Logica para enviar um email de alerta, por exemplo
    }
}
