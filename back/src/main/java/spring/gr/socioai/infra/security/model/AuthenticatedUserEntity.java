package spring.gr.socioai.infra.security.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import spring.gr.socioai.infra.mappers.EmailMapper;

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

    public AuthenticatedUserEntity(Email username, String password, UserRole role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public AuthenticatedUserEntity(Email username, String password) {
        this.username = username;
        this.password = password;
        this.role = UserRole.USER;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    @Convert(converter = EmailMapper.class)
    @Column(unique = true, length = 254) // Ver RFC 5321 para entender o tamanho do email
    private Email username;

    @Column(nullable = false, length = 30)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if(role == UserRole.ADMIN) return List.of(new SimpleGrantedAuthority("ROLE_ADMIN"));
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
