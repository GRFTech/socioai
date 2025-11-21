package spring.gr.socioai.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import spring.gr.socioai.model.LancamentoEntity;
import spring.gr.socioai.model.valueobjects.Email;

import java.util.List;
import java.util.UUID;

@Repository
public interface LancamentoRepository extends JpaRepository<LancamentoEntity, Long> {
    @Query("""
            SELECT COUNT(l) > 0  
            FROM LancamentoEntity l
            WHERE l.id = :id
              AND l.meta.categoria.user.id = :userId
            """)
    boolean isOwner(Long id, UUID userId);

    List<LancamentoEntity> getAllByMeta_Categoria_User_Username(Email metaCategoriaUserUsername);
}
