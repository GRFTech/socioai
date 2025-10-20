package spring.gr.socioai.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "CATEGORIA_TB")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CategoriaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 45, nullable = false)
    private String nome;

    @Column(length = 10, nullable = false)
    private String tipo;

    @ManyToOne(fetch = FetchType.LAZY)
    private AuthenticatedUserEntity user;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "categoria")
    private List<MetaEntity> metas = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "categoria")
    private List<DespesaEntity> despesas = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "categoria")
    private List<ReceitaEntity> receitas = new ArrayList<>();
}
