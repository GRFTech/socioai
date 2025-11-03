package spring.gr.socioai.controller.http.responses;

import java.time.LocalDateTime;

public record LancamentoResponse(
        Long id,
        String descricao,
        Double valor,
        String tipoLancamento,
        LocalDateTime dataCriacao,
        Long microCategoriaId
) {}
