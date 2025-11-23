package spring.gr.socioai.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.query.Meta;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import spring.gr.socioai.controller.http.requests.CategoriaDTO;
import spring.gr.socioai.controller.http.responses.CategoriaResponse;
import spring.gr.socioai.controller.http.responses.TotalCategoriaResponse;
import spring.gr.socioai.model.CategoriaEntity;
import spring.gr.socioai.model.MetaEntity;
import spring.gr.socioai.model.valueobjects.Email;
import spring.gr.socioai.repository.AuthenticatedUserRepository;
import spring.gr.socioai.repository.CategoriaRepository;
import spring.gr.socioai.repository.MetaRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class CategoriaService {

    private final CategoriaRepository repository;
    private final AuthenticatedUserRepository authenticatedUserRepository;

    /**
     * Converte um CategoriaDTO em uma entidade Categoria para persistência.
     * Nota: O modelo Categoria possui relacionamentos {@code user}, {@code metas},
     * {@code despesas}, e {@code receitas}. Para simplificar o CRUD básico,
     * estes são inicializados como nulos/vazios. Se o campo {@code user} for obrigatório,
     * você precisará modificar o DTO/serviço para incluir o ID do usuário autenticado.
     *
     * @param dto O DTO contendo nome e tipo da categoria.
     * @return A entidade Categoria pronta para ser salva.
     */
    public CategoriaEntity convertToEntity(CategoriaDTO dto) {
        return new CategoriaEntity(
                null,
                dto.getNome(),
                authenticatedUserRepository.findByUsername(new Email(dto.getUsername())).orElseThrow(()
                        -> new NoSuchElementException("Nenhum usuário com esse nome encontrado")),
                null
        );
    }

    /**
     * Salva uma nova Categoria no banco de dados.
     *
     * @param categoriaDTO DTO contendo nome e tipo da categoria a ser salva.
     * @return A Categoria salva, incluindo o ID gerado.
     */
    @Transactional
    @PreAuthorize("#categoriaDTO.username == authentication.name")
    public CategoriaResponse save(CategoriaDTO categoriaDTO) {
        CategoriaEntity novaCategoria = convertToEntity(categoriaDTO);
        return toResponse(repository.save(novaCategoria));
    }

    /**
     * Salva uma lista de novas Categorias no banco de dados.
     *
     * @param categoriasDTO Lista de DTOs contendo os dados das Categorias a serem salvas.
     * @return Lista das Categorias salvas, cada uma com seu ID gerado.
     */
    @Transactional
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<CategoriaResponse> saveAll(List<CategoriaDTO> categoriasDTO) {
        List<CategoriaEntity> categorias = categoriasDTO.stream()
                .map(this::convertToEntity)
                .toList();
        return repository.saveAll(categorias).stream().map(this::toResponse).toList();
    }

    /**
     * Atualiza uma Categoria existente com base no ID.
     *
     * @param id O ID da Categoria a ser atualizada.
     * @param categoriaDTO DTO com os novos dados (nome e tipo).
     * @return A Categoria atualizada.
     * @throws NoSuchElementException se a Categoria com o ID fornecido não for encontrada.
     */
    @Transactional
    @PreAuthorize("hasPermission(#id, 'categoria', 'WRITE')")
    public CategoriaResponse update(Long id, CategoriaDTO categoriaDTO) {
        CategoriaEntity existingCategoria = repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Categoria com ID " + id + " não encontrada para atualização."));

        existingCategoria.setNome(categoriaDTO.getNome());

        return toResponse(repository.save(existingCategoria));
    }

    /**
     * Retorna todas as Categorias cadastradas no banco de dados.
     *
     * @return Uma lista de todas as Categoria.
     */
    @Transactional
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<CategoriaResponse> getAll() {
        return repository.findAll().stream().map(this::toResponse).toList();
    }

    /**
     * Busca uma Categoria pelo seu ID.
     *
     * @param id O ID da Categoria a ser buscada.
     * @return Um Optional contendo a Categoria se encontrada, ou um Optional vazio.
     */
    @Transactional
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public CategoriaResponse getByID(Long id) {

        var c = repository.findById(id).orElseThrow(() -> new NoSuchElementException("Categoria não encontrada!"));

        return toResponse(c);
    }

    /**
     * Deleta uma Categoria específica pelo seu ID.
     *
     * @param id O ID da Categoria a ser deletada.
     * @throws NoSuchElementException se a Categoria com o ID fornecido não for encontrada.
     */
    @Transactional
    @PreAuthorize("hasPermission(#id, 'categoria', 'DELETE')")
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new NoSuchElementException("Categoria com ID " + id + " não encontrada para remoção");
        }
        repository.deleteById(id);
    }

    /**
     * Deleta uma lista de Categorias com base em seus IDs.
     *
     * @param ids Lista de IDs das Categorias a serem deletadas.
     */
    @Transactional
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void deleteAll(List<Long> ids) {
        repository.deleteAllById(ids);
    }


    private CategoriaResponse toResponse(CategoriaEntity entity) {
        return new CategoriaResponse(
                entity.getId(),
                entity.getNome(),
                entity.getUser().getUsername(),
                entity.getMetas() == null ? null : entity.getMetas().stream().map(MetaEntity::getId).toList()
        );
    }

    /**
     * Busca todas as categorias atreladas a um usuário no repositório
     * e transforma em resposta para envio ao front-end
     *
     * @param username Username do usuário
     * @return uma lista com as respostas prontas pra envio
     */
    @Transactional
    @PreAuthorize("#username == authentication.name or hasRole('ROLE_ADMIN')")
    public List<CategoriaResponse> getAllCategoriasByUsername(String username) {

        this.authenticatedUserRepository.findByUsername(new Email(username))
                .orElseThrow(() -> new EntityNotFoundException("Usuário com username: '" + username + "' não encontrado"));

        var list = this.repository.getAllByUser_Username(new Email(username));

        return list.stream().map(this::toResponse).toList();
    }

    /**
     * Calcula o valor total acumulado das metas para cada categoria associada a um usuário específico.
     * <p>
     * Este método recupera todas as categorias do usuário, itera sobre as metas configuradas
     * em cada categoria e realiza o somatório dos valores atuais.
     * </p>
     *
     * @param username O nome de usuário (geralmente o e-mail) utilizado para buscar as categorias.
     * Deve corresponder ao usuário autenticado no contexto de segurança.
     * @return Uma lista de {@link TotalCategoriaResponse} contendo o nome da categoria e a soma total
     * dos valores das metas daquela categoria. Retorna uma lista vazia se o usuário não possuir categorias.
     * @throws IllegalArgumentException se o formato do username (Email) for inválido ao instanciar o objeto Email.
     * * @see TotalCategoriaResponse
     * @see #repository
     */
    @Transactional
    @PreAuthorize("#username == authentication.name")
    public List<TotalCategoriaResponse> valorTotalCategoriaByUsername(String username) {
        var allCategoriasByUsername = this.repository.getAllByUser_Username(new Email(username));

        return allCategoriasByUsername.stream()
                .map(categoria -> {
                    Double soma = categoria.getMetas().stream()
                            .mapToDouble(MetaEntity::getValorAtual)
                            .sum();

                    return new TotalCategoriaResponse(categoria.getNome(), soma);
                })
                .toList();
    }


    public CategoriaEntity findByID(Long id) {
        return repository.getReferenceById(id);
    }
}
