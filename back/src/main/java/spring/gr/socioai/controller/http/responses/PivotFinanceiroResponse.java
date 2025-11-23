package spring.gr.socioai.controller.http.responses;

public record PivotFinanceiroResponse(
        String periodo, // Ex: "2023-11"
        Double totalReceitas,
        Double totalDespesas,
        Double saldoLiquido
) {}
