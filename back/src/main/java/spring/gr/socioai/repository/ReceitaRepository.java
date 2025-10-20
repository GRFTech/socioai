package spring.gr.socioai.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import spring.gr.socioai.model.ReceitaEntity;

@Repository
public interface ReceitaRepository extends JpaRepository<ReceitaEntity,Long> {
}
