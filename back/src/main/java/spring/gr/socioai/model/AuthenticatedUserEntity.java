package spring.gr.socioai.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import spring.gr.socioai.model.valueobjects.Email;
import spring.gr.socioai.model.valueobjects.UserRole;
import spring.gr.socioai.service.mappers.EmailMapper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Entity
@Table(
        name = "USER_TB",
        indexes = {
                @Index(name = "idx_user_email", columnList = "username")
        })
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticatedUserEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    @Convert(converter = EmailMapper.class)
    @Column(unique = true, length = 254) // Ver RFC 5321 para entender o tamanho do email
    private Email username;

    @Column(nullable = false, length = 30)
    private String password;

    @ManyToOne(fetch = FetchType.LAZY)
    private AuthenticatedUserRoleEntity role;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private List<CategoriaEntity> categorias = new ArrayList<>();

    public AuthenticatedUserEntity(Email email, String encode) {
        this.username = email;
        this.password = encode;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if(role.getDescription().equals(UserRole.ADMIN.getRole())) return List.of(new SimpleGrantedAuthority("ROLE_ADMIN"));
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username.getEmail();
    }
}
