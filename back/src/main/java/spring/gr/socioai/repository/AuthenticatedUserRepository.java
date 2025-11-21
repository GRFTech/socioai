package spring.gr.socioai.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import spring.gr.socioai.model.AuthenticatedUserEntity;
import spring.gr.socioai.model.valueobjects.Email;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AuthenticatedUserRepository extends JpaRepository<AuthenticatedUserEntity, UUID> {

    Optional<AuthenticatedUserEntity> findByUsername(Email username);
    void deleteByUsername(Email username);

    boolean existsByUsername(Email username);
}
