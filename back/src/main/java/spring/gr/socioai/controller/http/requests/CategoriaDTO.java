package spring.gr.socioai.controller.http.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * DTO (Data Transfer Object) usado para receber dados de criação e atualização
 * da entidade Categoria, contendo o nome e o tipo da categoria.
 */
@Data
public class CategoriaDTO {

    @NotBlank(message = "O nome da categoria é obrigatório")
    @Size(max = 45, message = "O nome deve ter no máximo 45 caracteres")
    private String nome;

    @NotBlank(message = "O nome do usuário não pode estar em branco")
    private String username;

}
