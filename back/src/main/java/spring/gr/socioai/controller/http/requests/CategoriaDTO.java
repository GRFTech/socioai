package spring.gr.socioai.controller.http.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.UUID;

/**
 * DTO (Data Transfer Object) usado para receber dados de criação e atualização
 * da entidade Categoria, contendo o nome e o tipo da categoria.
 */
@Data
public class CategoriaDTO {

    /**
     * O nome da categoria. Deve ser obrigatório e ter no máximo 45 caracteres.
     */
    @NotBlank(message = "O nome da categoria é obrigatório")
    @Size(max = 45, message = "O nome deve ter no máximo 45 caracteres")
    private String nome;

    /**
     * O tipo da categoria (ex: Receita, Despesa). Deve ser obrigatório e ter no máximo 10 caracteres.
     */
    @NotBlank(message = "O tipo da categoria é obrigatório")
    @Size(max = 10, message = "O tipo deve ter no máximo 10 caracteres")
    private String tipo;

}
