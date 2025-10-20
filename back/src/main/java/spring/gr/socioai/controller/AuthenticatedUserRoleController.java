package spring.gr.socioai.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import spring.gr.socioai.controller.http.requests.AuthenticatedUserRoleDTO;
import spring.gr.socioai.model.AuthenticatedUserRoleEntity;
import spring.gr.socioai.service.AuthenticatedUserRoleService;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/roles")
public class AuthenticatedUserRoleController {

    private final AuthenticatedUserRoleService service;

    /**
     * Endpoint para salvar uma nova Role.
     * Mapeado para HTTP POST /api/roles
     *
     * @param roleDTO DTO contendo a descrição da nova Role.
     * @return ResponseEntity com a Role salva e status 201 (CREATED).
     */
    @PostMapping
    public ResponseEntity<AuthenticatedUserRoleEntity> save(@Valid @RequestBody AuthenticatedUserRoleDTO roleDTO) {
        AuthenticatedUserRoleEntity savedRole = service.save(roleDTO);
        return new ResponseEntity<>(savedRole, HttpStatus.CREATED);
    }

    /**
     * Endpoint para salvar uma lista de Roles em lote.
     * Mapeado para HTTP POST /api/roles/batch
     *
     * @param rolesDTO Lista de DTOs das Roles a serem salvas.
     * @return ResponseEntity com a lista de Roles salvas e status 201 (CREATED).
     */
    @PostMapping("/batch")
    public ResponseEntity<List<AuthenticatedUserRoleEntity>> saveAll(@Valid @RequestBody List<AuthenticatedUserRoleDTO> rolesDTO) {
        List<AuthenticatedUserRoleEntity> savedRoles = service.saveAll(rolesDTO);
        return new ResponseEntity<>(savedRoles, HttpStatus.CREATED);
    }

    /**
     * Endpoint para atualizar uma Role existente.
     * Mapeado para HTTP PUT /api/roles/{id}
     *
     * @param id O ID da Role a ser atualizada.
     * @param roleDTO DTO com os novos dados.
     * @return ResponseEntity com a Role atualizada e status 200 (OK),
     * ou status 404 (NOT FOUND) se o ID não existir.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AuthenticatedUserRoleEntity> update(@PathVariable Long id, @Valid @RequestBody AuthenticatedUserRoleDTO roleDTO) {
        try {
            AuthenticatedUserRoleEntity updatedRole = service.update(id, roleDTO);
            return ResponseEntity.ok(updatedRole);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Endpoint para retornar todas as Roles.
     * Mapeado para HTTP GET /api/roles
     *
     * @return ResponseEntity com a lista de todas as Roles e status 200 (OK).
     */
    @GetMapping
    public ResponseEntity<List<AuthenticatedUserRoleEntity>> getAll() {
        List<AuthenticatedUserRoleEntity> roles = service.getAll();
        return ResponseEntity.ok(roles);
    }

    /**
     * Endpoint para buscar uma Role pelo seu ID.
     * Mapeado para HTTP GET /api/roles/{id}
     *
     * @param id O ID da Role a ser buscada.
     * @return ResponseEntity com a Role e status 200 (OK),
     * ou status 404 (NOT FOUND) se a Role não for encontrada.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AuthenticatedUserRoleEntity> getByID(@PathVariable Long id) {
        return service.getByID(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Endpoint para deletar uma Role pelo seu ID.
     * Mapeado para HTTP DELETE /api/roles/{id}
     *
     * @param id O ID da Role a ser deletada.
     * @return ResponseEntity com status 204 (NO CONTENT) se a deleção for bem-sucedida,
     * ou status 404 (NOT FOUND) se o ID não existir.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            service.delete(id);
            return ResponseEntity.noContent().build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Endpoint para deletar uma lista de Roles com base em seus IDs.
     * Mapeado para HTTP DELETE /api/roles/batch
     *
     * @param ids Lista de IDs (Long) das Roles a serem deletadas, enviada no corpo da requisição.
     * @return ResponseEntity com status 204 (NO CONTENT) se a deleção for bem-sucedida,
     * ou status 400 (BAD REQUEST) se a lista de IDs estiver vazia.
     */
    @DeleteMapping("/batch")
    public ResponseEntity<Void> deleteAll(@RequestBody List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        service.deleteAll(ids);
        return ResponseEntity.noContent().build();
    }
}
