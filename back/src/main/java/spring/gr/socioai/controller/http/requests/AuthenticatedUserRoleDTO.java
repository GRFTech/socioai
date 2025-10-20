package spring.gr.socioai.controller.http.requests;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AuthenticatedUserRoleDTO {

    @NotBlank(message = "A descrição é obrigatória")
    private String description;
}