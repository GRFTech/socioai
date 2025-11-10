package spring.gr.socioai.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import spring.gr.socioai.model.CategoriaMicroEntity;
import spring.gr.socioai.repository.CategoriaMicroRepository;

@Service
@RequiredArgsConstructor
public class CategoriaMicroService {

    private final CategoriaMicroRepository repository;

    public CategoriaMicroEntity save(CategoriaMicroEntity categoriaMicroEntity) {
        return repository.save(categoriaMicroEntity);
    }
}
