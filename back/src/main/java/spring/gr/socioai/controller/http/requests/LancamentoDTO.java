package spring.gr.socioai.controller.http.requests;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class LancamentoDTO {

    @NotNull(message = "A receita deve ter uma descrição")
    private String descricao;

    @NotNull(message = "O valor da receita é obrigatório")
    @Positive(message = "O valor deve ser positivo")
    private Double valor;

    @NotNull(message = "A data de criação é obrigatória")
    private LocalDateTime dataCriacao;

    @NotNull(message = "O tipo do lançamento é obrigatório")
    private String tipoLancamento;

    @PositiveOrZero(message = "O id da categoria é maior ou igual a zero")
    private Long microCategoriaId;
}