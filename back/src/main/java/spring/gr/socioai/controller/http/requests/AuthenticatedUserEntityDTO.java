package spring.gr.socioai.controller.http.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AuthenticatedUserEntityDTO {

    /**
     * O email (username) do usuário. Deve ser um formato de email válido e é obrigatório.
     */
    @NotBlank(message = "O email é obrigatório")
    @Email(message = "Formato de email inválido")
    @Size(max = 254, message = "O email deve ter no máximo 254 caracteres")
    private String username;

    /**
     * A senha do usuário. Deve ser obrigatória e será codificada (hashed) no Service.
     */
    @NotBlank(message = "A senha é obrigatória")
    @Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres")
    private String password;

    /**
     * O ID (Long) da Role (papel) associada a este usuário.
     * Deve referenciar uma AuthenticatedUserRoleEntity existente.
     */
    @NotNull(message = "O ID da Role é obrigatório")
    private Long roleId;
}
