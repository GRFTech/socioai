package spring.gr.socioai.controller.http.responses;

import java.time.LocalDateTime;

public record DespesaResponse(
        Long id,
        String descricao,
        Double valor,
        LocalDateTime dataCriacao,
        Long categoria
) {}