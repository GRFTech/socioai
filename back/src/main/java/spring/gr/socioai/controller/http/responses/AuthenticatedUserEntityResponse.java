package spring.gr.socioai.controller.http.responses;

import java.util.UUID;

public record AuthenticatedUserEntityResponse(
        UUID id,
        String username,
        Long role
) {}