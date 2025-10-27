package spring.gr.socioai.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import spring.gr.socioai.controller.http.requests.AuthenticatedUserEntityDTO;
import spring.gr.socioai.controller.http.responses.AuthenticatedUserEntityResponse;
import spring.gr.socioai.model.AuthenticatedUserEntity;
import spring.gr.socioai.model.valueobjects.Email;
import spring.gr.socioai.repository.AuthenticatedUserRepository;
import spring.gr.socioai.repository.AuthenticatedUserRoleRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthenticatedUserService {
    private final AuthenticatedUserRepository userRepository;
    private final AuthenticatedUserRoleRepository userRoleRepository;
    private final AuthenticatedUserRoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Atualiza um Usuário existente com base no Username.
     *
     * @param username O username do Usuário a ser atualizado.
     * @param userDTO DTO com os novos dados (username, password, roleId).
     * @return O Usuário atualizado.
     * @throws NoSuchElementException se o Usuário ou a nova Role não for encontrada.
     */
    @Transactional
    public AuthenticatedUserEntityResponse update(String username, AuthenticatedUserEntityDTO userDTO) {
        var existingUser = userRepository.findByUsername(new Email(username))
                .orElseThrow(() -> new NoSuchElementException("Usuário com username " + username + " não encontrado para atualização."));

        var newRole = roleRepository.getReferenceById(userDTO.getRoleId());

        existingUser.setUsername(new Email(userDTO.getUsername()));
        existingUser.setPassword(passwordEncoder.encode(userDTO.getPassword()));

        if(!this.roleRepository.existsById(userDTO.getRoleId())) {
            throw new IllegalArgumentException("A role de ID " + userDTO.getRoleId() + " não existe!");
        }

        existingUser.setRole(newRole);

        var u = userRepository.save(existingUser);

        // possível lógica pra chamar o envio de um email usando mensageria assíncrona

        return toResponse(u);
    }

    /**
     * Retorna todos os Usuários cadastrados no banco de dados.
     *
     * @return Uma lista de todos os AuthenticatedUserEntity.
     */
    @Transactional
    public List<AuthenticatedUserEntityResponse> getAll() {
        var  allUsers = userRepository.findAll();
        return allUsers.stream().map(this::toResponse).toList();
    }

    /**
     * Busca um Usuário pelo seu ID.
     *
     * @param id O ID (UUID) do Usuário a ser buscado.
     * @return Um Optional contendo o Usuário se encontrado, ou um Optional vazio.
     */
    @Transactional
    public Optional<AuthenticatedUserEntityResponse> getByID(UUID id) {
        var u = userRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Usuário não encontrado!"));
        return Optional.of(toResponse(u));
    }

    /**
     * Busca um Usuário pelo seu Username.
     *
     * @param username O username a ser buscado
     * @return Um Optional contendo o Usuário se encontrado, ou uma exception
     */
    @Transactional
    public Optional<AuthenticatedUserEntityResponse> getByUsername(String username) {
        var email = new Email(username);
        var u = userRepository.findByUsername(email).orElseThrow(() -> new NoSuchElementException("Usuário não encontrado!"));
        return Optional.of(toResponse(u));
    }

    /**
     * Deleta um Usuário específico pelo seu ID.
     *
     * @param id O ID (UUID) do Usuário a ser deletado.
     * @throws NoSuchElementException se o Usuário com o ID fornecido não for encontrado.
     */
    @Transactional
    public void delete(UUID id) {
        if (!userRepository.existsById(id)) {
            throw new NoSuchElementException("Usuário com ID " + id + " não encontrado para deleção.");
        }
        userRepository.deleteById(id);
    }

    /**
     * Deleta uma lista de Usuários com base em seus IDs.
     *
     * @param ids Lista de IDs (UUID) dos Usuários a serem deletados.
     */
    @Transactional
    public void deleteAll(List<UUID> ids) {
        userRepository.deleteAllById(ids);
    }


    /**
     * Transforma uma entidade de banco em um objeto serializável
     *
     * @param user é a entidade de banco
     * @return AuthenticatedUserEntityResponse
     */
    public AuthenticatedUserEntityResponse toResponse(AuthenticatedUserEntity user) {
        return new AuthenticatedUserEntityResponse(user.getUsername(), user.getRole().getId());
    }
}
