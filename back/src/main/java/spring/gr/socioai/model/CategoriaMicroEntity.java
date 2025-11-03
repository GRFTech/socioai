package spring.gr.socioai.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Table
@Entity(name = "CATEGORIA_MICRO_TB")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CategoriaMicroEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meta_id")
    private MetaEntity meta;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_id")
    private CategoriaEntity categoria;

    @OneToMany(fetch = FetchType.LAZY)
    private List<LancamentoEntity> lancamentos;

}
