package spring.gr.socioai.controller.http.responses;

public record AuthenticatedUserEntityResponse(
        String username,
        Long role
) {}