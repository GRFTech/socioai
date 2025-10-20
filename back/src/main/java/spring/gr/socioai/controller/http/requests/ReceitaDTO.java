package spring.gr.socioai.controller.http.requests;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReceitaDTO {

    /**
     * O valor da receita. Deve ser obrigatório e positivo.
     */
    @NotNull(message = "O valor da receita é obrigatório")
    @Positive(message = "O valor deve ser positivo")
    private Double valor;

    /**
     * A data e hora da criação (ou ocorrência) da receita. Deve ser obrigatória.
     */
    @NotNull(message = "A data de criação é obrigatória")
    private LocalDateTime dataCriacao;
}