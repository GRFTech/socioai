package spring.gr.socioai.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import spring.gr.socioai.model.DespesaEntity;

@Repository
public interface DespesaRepository extends JpaRepository<DespesaEntity, Long> {
}