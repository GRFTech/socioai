package spring.gr.socioai.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import spring.gr.socioai.model.LancamentoEntity;
import spring.gr.socioai.model.dto.ResumoLancamentoDTO;
import spring.gr.socioai.model.valueobjects.Email;

import java.time.LocalDateTime;
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

    @Query("""
        SELECT new spring.gr.socioai.model.dto.ResumoLancamentoDTO(
            YEAR(l.dataCriacao), 
            MONTH(l.dataCriacao), 
            l.tipoLancamento, 
            SUM(l.valor)
        )
        FROM LancamentoEntity l
        WHERE l.meta.categoria.user.username = :username
        GROUP BY YEAR(l.dataCriacao), MONTH(l.dataCriacao), l.tipoLancamento
        ORDER BY YEAR(l.dataCriacao) DESC, MONTH(l.dataCriacao) DESC
    """)
    List<ResumoLancamentoDTO> agruparLancamentosPorUsuario(@Param("username") Email username);

    @Query("""
        SELECT new spring.gr.socioai.model.dto.ResumoLancamentoDTO(
            YEAR(l.dataCriacao), 
            MONTH(l.dataCriacao), 
            l.tipoLancamento, 
            SUM(l.valor)
        )
        FROM LancamentoEntity l
        WHERE l.meta.categoria.user.username = :username
          AND l.dataCriacao BETWEEN :inicio AND :fim
        GROUP BY YEAR(l.dataCriacao), MONTH(l.dataCriacao), l.tipoLancamento
        ORDER BY YEAR(l.dataCriacao) DESC, MONTH(l.dataCriacao) DESC
    """)
    List<ResumoLancamentoDTO> agruparLancamentosPorPeriodo(
            @Param("username") Email username,
            @Param("inicio") LocalDateTime inicio,
            @Param("fim") LocalDateTime fim
    );

    @Query("""
        SELECT new spring.gr.socioai.model.dto.ResumoLancamentoDTO(
            YEAR(l.dataCriacao), 
            MONTH(l.dataCriacao), 
            l.tipoLancamento, 
            SUM(l.valor)
        )
        FROM LancamentoEntity l
        GROUP BY YEAR(l.dataCriacao), MONTH(l.dataCriacao), l.tipoLancamento
        ORDER BY YEAR(l.dataCriacao) DESC, MONTH(l.dataCriacao) DESC
    """)
    List<ResumoLancamentoDTO> agruparLancamentosGlobal();
}
