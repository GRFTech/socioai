package spring.gr.socioai.controller.http.responses;

public record CategoriaResponse(
        Long id,
        String nome,
        String tipo,
        String user
) {}