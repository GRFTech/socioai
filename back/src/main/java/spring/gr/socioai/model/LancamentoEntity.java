package spring.gr.socioai.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import spring.gr.socioai.model.valueobjects.TipoLancamento;

import java.time.LocalDateTime;

@Entity
@Table(name = "LANCAMENTOS_TB")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LancamentoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false)
    private String descricao;

    @Column(nullable = false)
    private Double valor;

    @Enumerated(EnumType.ORDINAL)
    @Column(nullable = false, name = "tipo_lancamento")
    private TipoLancamento tipoLancamento;

    @Column(nullable = false)
    private LocalDateTime dataCriacao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_micro_id")
    private CategoriaMicroEntity categoriaMicro;
}
