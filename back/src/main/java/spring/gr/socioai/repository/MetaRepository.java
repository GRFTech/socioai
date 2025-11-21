package spring.gr.socioai.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import spring.gr.socioai.model.MetaEntity;

import java.util.UUID;

@Repository
public interface MetaRepository extends JpaRepository<MetaEntity, Long> {
    @Query("""
            SELECT COUNT(m) > 0  
                FROM MetaEntity m
                WHERE m.id = :id
                    AND m.categoria.user.id = :userId
            """)
    boolean isOwner(Long id, UUID userId);
}
