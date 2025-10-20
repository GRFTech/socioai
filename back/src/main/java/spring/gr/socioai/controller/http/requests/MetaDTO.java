package spring.gr.socioai.controller.http.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
public class MetaDTO {

    @NotBlank(message = "A descrição da meta é obrigatória")
    @Size(max = 45, message = "A descrição deve ter no máximo 45 caracteres")
    private String descricao;

    @NotNull(message = "O valor atual é obrigatório")
    @PositiveOrZero(message = "O valor atual não pode ser negativo")
    private Double valorAtual;

    @NotNull(message = "A data de início é obrigatória")
    private LocalDate dataInicio;

    @NotNull(message = "A data de fim é obrigatória")
    private LocalDate dataFim;
}
