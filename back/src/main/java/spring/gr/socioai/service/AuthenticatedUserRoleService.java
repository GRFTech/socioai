package spring.gr.socioai.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import spring.gr.socioai.controller.http.requests.AuthenticatedUserRoleDTO;
import spring.gr.socioai.controller.http.responses.AuthenticatedUserRoleResponse;
import spring.gr.socioai.model.AuthenticatedUserRoleEntity;
import spring.gr.socioai.repository.AuthenticatedUserRoleRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class AuthenticatedUserRoleService {

    private final AuthenticatedUserRoleRepository repository;

    /**
     * Salva uma nova Role no banco de dados.
     *
     * @param roleDTO DTO contendo a descrição da Role a ser salva.
     * @return A Role salva, incluindo o ID gerado.
     */
    @Transactional
    public AuthenticatedUserRoleResponse save(AuthenticatedUserRoleDTO roleDTO) {
        var newRole = new AuthenticatedUserRoleEntity(null, roleDTO.getDescription(), null);
        var s = repository.save(newRole);
        return toResponse(s);
    }

    /**
     * Salva uma lista de novas Roles no banco de dados.
     *
     * @param rolesDTO Lista de DTOs contendo as descrições das Roles a serem salvas.
     * @return Lista das Roles salvas, cada uma com seu ID gerado.
     */
    @Transactional
    public List<AuthenticatedUserRoleResponse> saveAll(List<AuthenticatedUserRoleDTO> rolesDTO) {
        List<AuthenticatedUserRoleEntity> roles = rolesDTO.stream()
                .map(dto -> new AuthenticatedUserRoleEntity(null, dto.getDescription(), null))
                .toList();
        var rs = repository.saveAll(roles);

        return rs.stream().map(this::toResponse).toList();
    }

    /**
     * Atualiza uma Role existente com base no ID.
     *
     * @param id O ID da Role a ser atualizada.
     * @param roleDTO DTO com os novos dados de descrição.
     * @return A Role atualizada.
     * @throws NoSuchElementException se a Role com o ID fornecido não for encontrada.
     */
    @Transactional
    public AuthenticatedUserRoleResponse update(Long id, AuthenticatedUserRoleDTO roleDTO) {
        var existingRole = repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Role com ID " + id + " não encontrado para atualização."));

        existingRole.setDescription(roleDTO.getDescription());

        var rs = repository.save(existingRole);

        return toResponse(rs);
    }

    /**
     * Retorna todas as Roles cadastradas no banco de dados.
     *
     * @return Uma lista de todas as AuthenticatedUserRoleEntity.
     */
    @Transactional
    public List<AuthenticatedUserRoleResponse> getAll() {
        var roles = repository.findAll();
        return roles.stream().map(this::toResponse).toList();
    }

    /**
     * Busca uma Role pelo seu ID.
     *
     * @param id O ID da Role a ser buscada.
     * @return Um Optional contendo a Role se encontrada, ou um Optional vazio.
     */
    @Transactional
    public Optional<AuthenticatedUserRoleResponse> getByID(Long id) {
        var r = repository.findById(id).orElseThrow(() -> new NoSuchElementException("Elemento não encontrado"));
        return Optional.of(this.toResponse(r));
    }

    /**
     * Deleta uma Role específica pelo seu ID.
     *
     * @param id O ID da Role a ser deletada.
     * @throws NoSuchElementException se a Role com o ID fornecido não for encontrada.
     */
    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new NoSuchElementException("Role com ID " + id + " não encontrado para deleção.");
        }
        repository.deleteById(id);
    }

    /**
     * Deleta uma lista de Roles com base em seus IDs.
     *
     * @param ids Lista de IDs das Roles a serem deletadas.
     */
    @Transactional
    public void deleteAll(List<Long> ids) {
        repository.deleteAllById(ids);
    }

    /**
     * Transforma um objeto de entidade em um pronto para serialização
     *
     * @param role
     * @return AuthenticatedUserRoleResponse
     */
    public AuthenticatedUserRoleResponse toResponse(AuthenticatedUserRoleEntity role) {
        return new AuthenticatedUserRoleResponse(role.getId(), role.getDescription());
    }
}
