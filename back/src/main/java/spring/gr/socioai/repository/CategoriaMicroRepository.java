package spring.gr.socioai.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import spring.gr.socioai.model.CategoriaMicroEntity;

@Repository
public interface CategoriaMicroRepository extends JpaRepository<CategoriaMicroEntity, Long> {
}
