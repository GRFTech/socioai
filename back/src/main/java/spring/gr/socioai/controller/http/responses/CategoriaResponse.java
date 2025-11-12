package spring.gr.socioai.controller.http.responses;

import java.util.List;

public record CategoriaResponse(
        Long id,
        String nome,
        String user,
        List<Long> metas
) {}