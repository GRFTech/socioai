package spring.gr.socioai.infra.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import spring.gr.socioai.infra.security.model.AuthenticatedUserEntity;
import spring.gr.socioai.infra.security.model.Email;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AuthenticatedUserRepository extends JpaRepository<AuthenticatedUserEntity, UUID> {

    Optional<AuthenticatedUserEntity> findByUsername(Email username);
    void deleteByUsername(Email username);
}
