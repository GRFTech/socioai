package spring.gr.socioai.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ROLES_TB")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticatedUserRoleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 15, nullable = false)
    private String description;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "role")
    private List<AuthenticatedUserEntity> users = new ArrayList<>();
}
