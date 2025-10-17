package spring.gr.socioai.interfaces.http.requests;

import spring.gr.socioai.infra.security.model.Email;

public record LoginRequest(Email email, String password) {
}
