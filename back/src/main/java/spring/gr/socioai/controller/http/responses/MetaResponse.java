package spring.gr.socioai.controller.http.responses;

import java.time.LocalDate;

public record MetaResponse(
        Long id,
        String descricao,
        Double valorAtual,
        LocalDate dataInicio,
        LocalDate dataFim,
        Long categoria
) {}
