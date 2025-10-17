package spring.gr.socioai.infra.security.events;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.security.authorization.event.AuthorizationDeniedEvent;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AuthorizationEvents {

    @EventListener
    public void onFailure(AuthorizationDeniedEvent event) {
        log.info("Authorization Denied for user: {}", event.getAuthentication().get().getName());

        // Evento que faça sentido pra app
    }
}
