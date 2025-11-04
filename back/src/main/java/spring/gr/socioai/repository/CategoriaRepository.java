package spring.gr.socioai.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import spring.gr.socioai.model.CategoriaEntity;
import spring.gr.socioai.model.valueobjects.Email;

import java.util.List;

@Repository
public interface CategoriaRepository extends JpaRepository<CategoriaEntity, Long> {
    List<CategoriaEntity> getAllByUser_Username(Email userUsername);
}
