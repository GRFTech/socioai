package spring.gr.socioai.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import spring.gr.socioai.model.AuthenticatedUserRoleEntity;

@Repository
public interface AuthenticatedUserRoleRepository extends JpaRepository<AuthenticatedUserRoleEntity, Long> {
}
