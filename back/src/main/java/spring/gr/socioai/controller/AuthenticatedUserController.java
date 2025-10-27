package spring.gr.socioai.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import spring.gr.socioai.controller.http.requests.AuthenticatedUserEntityDTO;
import spring.gr.socioai.controller.http.responses.AuthenticatedUserEntityResponse;
import spring.gr.socioai.service.AuthenticatedUserService;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@RestController
@RequestMapping("api/users")
@RequiredArgsConstructor
public class AuthenticatedUserController {

    private final AuthenticatedUserService authenticatedUserService;

    /**
     * Retorna todos os Usuários.
     * Mapeia para: GET /api/users
     * @return Lista de AuthenticatedUserEntityResponse com status 200 OK.
     */
    @GetMapping
    public ResponseEntity<List<AuthenticatedUserEntityResponse>> getAllUsers() {
        List<AuthenticatedUserEntityResponse> users = authenticatedUserService.getAll();
        return ResponseEntity.ok(users);
    }

    /**
     * Busca um Usuário pelo ID.
     * Mapeia para: GET /api/users/{id}
     * @param id O ID (UUID) do Usuário.
     * @return AuthenticatedUserEntityResponse com status 200 OK.
     * @throws ResponseStatusException 404 Not Found se o usuário não for encontrado.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AuthenticatedUserEntityResponse> getUserByID(@PathVariable UUID id) {
        try {
            AuthenticatedUserEntityResponse userResponse = authenticatedUserService.getByID(id)
                    .orElseThrow(() -> new NoSuchElementException("Usuário com ID " + id + " não encontrado."));
            return ResponseEntity.ok(userResponse);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    /**
     * Busca um Usuário pelo Username (Email).
     * Mapeia para: GET /api/users/username/{username}
     * @param username O username (email) do Usuário.
     * @return AuthenticatedUserEntityResponse com status 200 OK.
     * @throws ResponseStatusException 404 Not Found se o usuário não for encontrado.
     */
    @GetMapping("/username/{username}")
    public ResponseEntity<AuthenticatedUserEntityResponse> getUserByUsername(@PathVariable String username) {
        try {
            AuthenticatedUserEntityResponse userResponse = authenticatedUserService.getByUsername(username)
                    .orElseThrow(() -> new NoSuchElementException("Usuário com username " + username + " não encontrado."));
            return ResponseEntity.ok(userResponse);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    /**
     * Atualiza um Usuário existente.
     * Mapeia para: PUT /api/users/{username}
     * @param username O username do Usuário a ser atualizado.
     * @param userDTO DTO com os novos dados do Usuário.
     * @return AuthenticatedUserEntityResponse atualizado com status 200 OK.
     * @throws ResponseStatusException 404 Not Found se o usuário ou a Role não for encontrada.
     * @throws ResponseStatusException 400 Bad Request se a requisição for inválida. (Lógica de validação pode ser adicionada ao DTO)
     */
    @PutMapping("/{username}")
    public ResponseEntity<AuthenticatedUserEntityResponse> update(@PathVariable String username,
                                                                  @RequestBody AuthenticatedUserEntityDTO userDTO) {
        try {
            AuthenticatedUserEntityResponse updatedUser = authenticatedUserService.update(username, userDTO);
            return ResponseEntity.ok(updatedUser);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Erro ao atualizar o usuário: " + e.getMessage(), e);
        }
    }

    /**
     * Deleta um Usuário pelo ID.
     * Mapeia para: DELETE /api/users/{id}
     * @param id O ID (UUID) do Usuário a ser deletado.
     * @return Resposta vazia com status 204 No Content.
     * @throws ResponseStatusException 404 Not Found se o usuário não for encontrado.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserByID(@PathVariable UUID id) {
        try {
            authenticatedUserService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    /**
     * Deleta uma lista de Usuários.
     * Mapeia para: DELETE /api/users?ids={id1},{id2},...
     * @param ids Lista de IDs (UUID) dos Usuários a serem deletados.
     * @return Resposta vazia com status 204 No Content.
     */
    @DeleteMapping
    public ResponseEntity<Void> deleteAllUsers(@RequestParam List<UUID> ids) {
        if (ids == null || ids.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A lista de IDs não pode estar vazia.");
        }
        authenticatedUserService.deleteAll(ids);
        return ResponseEntity.noContent().build();
    }
}