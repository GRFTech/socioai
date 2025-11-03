package spring.gr.socioai.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import spring.gr.socioai.controller.http.requests.CategoriaDTO;
import spring.gr.socioai.controller.http.responses.CategoriaResponse;
import spring.gr.socioai.model.CategoriaEntity;
import spring.gr.socioai.repository.AuthenticatedUserRepository;
import spring.gr.socioai.repository.CategoriaRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

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
    private CategoriaEntity convertToEntity(CategoriaDTO dto) {
        return new CategoriaEntity(
                null,
                dto.getNome(),
                dto.getTipo(),
                null,
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
    public CategoriaResponse update(Long id, CategoriaDTO categoriaDTO) {
        CategoriaEntity existingCategoria = repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Categoria com ID " + id + " não encontrada para atualização."));

        existingCategoria.setNome(categoriaDTO.getNome());
        existingCategoria.setTipo(categoriaDTO.getTipo());

        return toResponse(repository.save(existingCategoria));
    }

    /**
     * Retorna todas as Categorias cadastradas no banco de dados.
     *
     * @return Uma lista de todas as Categoria.
     */
    @Transactional
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
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new NoSuchElementException("Categoria com ID " + id + " não encontrada para deleção.");
        }
        repository.deleteById(id);
    }

    /**
     * Deleta uma lista de Categorias com base em seus IDs.
     *
     * @param ids Lista de IDs das Categorias a serem deletadas.
     */
    @Transactional
    public void deleteAll(List<Long> ids) {
        repository.deleteAllById(ids);
    }


    private CategoriaResponse toResponse(CategoriaEntity entity) {
        return new CategoriaResponse(entity.getId() ,entity.getNome(), entity.getTipo(), entity.getUser().getUsername());
    }
}
