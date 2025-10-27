package spring.gr.socioai.controller.http.responses;

import java.time.LocalDateTime;

public record ReceitaResponse(
        Long id,
        String descricao,
        Double valor,
        LocalDateTime dataCriacao,
        Long categoria
) {}
