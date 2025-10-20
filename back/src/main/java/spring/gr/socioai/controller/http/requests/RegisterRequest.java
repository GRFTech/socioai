package spring.gr.socioai.controller.http.requests;

import spring.gr.socioai.model.valueobjects.Email;

public record RegisterRequest(Email email, String password) {
}
