package spring.gr.socioai.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "DESPESAS_TB")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DespesaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double valor;

    private LocalDateTime dataCriacao;

    @ManyToOne(fetch = FetchType.LAZY)
    private CategoriaEntity categoria;
}
