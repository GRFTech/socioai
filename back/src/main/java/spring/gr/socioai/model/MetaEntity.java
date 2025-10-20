package spring.gr.socioai.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "METAS_TB")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MetaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 45, nullable = false)
    private String descricao;

    @Column(nullable = false)
    private Double valorAtual;

    @Column(nullable = false)
    private LocalDate dataInicio;

    @Column(nullable = false)
    private LocalDate dataFim;

    @ManyToOne(fetch = FetchType.LAZY)
    private CategoriaEntity categoria;
}
