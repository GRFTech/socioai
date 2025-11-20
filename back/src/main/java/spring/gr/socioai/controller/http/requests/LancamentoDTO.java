package spring.gr.socioai.controller.http.requests;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import spring.gr.socioai.util.annotations.notzero.NotZero;

import java.time.LocalDateTime;

@Data
public class LancamentoDTO {

    @NotBlank(message = "O lançamento deve ter uma descrição")
    private String descricao;

    @NotNull(message = "O valor do lançamento é obrigatório")
    @NotZero(message = "O valor não pode ser igual a zero")
    private Double valor;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime dataCriacao;

    @NotNull(message = "O id da meta é obrigatório")
    @Positive(message = "O id da meta é maior que zero")
    private Long meta;
}