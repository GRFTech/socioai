package spring.gr.socioai.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import spring.gr.socioai.model.MetaEntity;

@Repository
public interface MetaRepository extends JpaRepository<MetaEntity, Long> {
}
