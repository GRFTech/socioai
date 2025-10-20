package spring.gr.socioai.controller.http.requests;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DespesaDTO {

    @NotNull(message = "A despesa deve ter uma descrição")
    private String descricao;

    @NotNull(message = "O valor da despesa é obrigatório")
    @Positive(message = "O valor deve ser positivo")
    private Double valor;

    /**
     * Data enviada pelo cliente pra registrar o tempo local dele, pois se
     * estiver rodando em um servidor com um fuso horario diferente pode dar bo
     */
    @NotNull(message = "A data de criação é obrigatória")
    private LocalDateTime dataCriacao;
}