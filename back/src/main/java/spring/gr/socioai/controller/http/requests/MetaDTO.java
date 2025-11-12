package spring.gr.socioai.controller.http.requests;

import com.fasterxml.jackson.annotation.JsonFormat;
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

    @PositiveOrZero(message = "O valor atual não pode ser negativo")
    private Double valorAtual;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataInicio;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataFim;

    @NotNull(message = "A categoria não pode estar em branco")
    private Long categoria;
}
