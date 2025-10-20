package spring.gr.socioai.controller.http.requests;

import spring.gr.socioai.model.valueobjects.Email;

public record LoginRequest(Email email, String password) {
}
