package spring.gr.socioai.model.dto;

import spring.gr.socioai.model.valueobjects.TipoLancamento;

public record ResumoLancamentoDTO(
        int ano,
        int mes,
        TipoLancamento tipo,
        Double total
) {}