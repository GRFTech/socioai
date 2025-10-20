package spring.gr.socioai.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import spring.gr.socioai.controller.http.requests.CategoriaDTO;
import spring.gr.socioai.model.CategoriaEntity;
import spring.gr.socioai.repository.CategoriaRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoriaService {

    private final CategoriaRepository repository;

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
        // Usa o construtor AllArgsConstructor (com id=null, user=null e listas vazias)
        return new CategoriaEntity(
                null,
                dto.getNome(),
                dto.getTipo(),
                null, // user (ManyToOne) - deve ser preenchido com lógica de autenticação real
                null, // metas (OneToMany)
                null, // despesas (OneToMany)
                null  // receitas (OneToMany)
        );
    }

    /**
     * Salva uma nova Categoria no banco de dados.
     *
     * @param categoriaDTO DTO contendo nome e tipo da categoria a ser salva.
     * @return A Categoria salva, incluindo o ID gerado.
     */
    @Transactional
    public CategoriaEntity save(CategoriaDTO categoriaDTO) {
        CategoriaEntity novaCategoria = convertToEntity(categoriaDTO);
        return repository.save(novaCategoria);
    }

    /**
     * Salva uma lista de novas Categorias no banco de dados.
     *
     * @param categoriasDTO Lista de DTOs contendo os dados das Categorias a serem salvas.
     * @return Lista das Categorias salvas, cada uma com seu ID gerado.
     */
    @Transactional
    public List<CategoriaEntity> saveAll(List<CategoriaDTO> categoriasDTO) {
        List<CategoriaEntity> categorias = categoriasDTO.stream()
                .map(this::convertToEntity)
                .toList();
        return repository.saveAll(categorias);
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
    public CategoriaEntity update(Long id, CategoriaDTO categoriaDTO) {
        CategoriaEntity existingCategoria = repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Categoria com ID " + id + " não encontrada para atualização."));

        existingCategoria.setNome(categoriaDTO.getNome());
        existingCategoria.setTipo(categoriaDTO.getTipo());

        return repository.save(existingCategoria);
    }

    /**
     * Retorna todas as Categorias cadastradas no banco de dados.
     *
     * @return Uma lista de todas as Categoria.
     */
    @Transactional
    public List<CategoriaEntity> getAll() {
        return repository.findAll();
    }

    /**
     * Busca uma Categoria pelo seu ID.
     *
     * @param id O ID da Categoria a ser buscada.
     * @return Um Optional contendo a Categoria se encontrada, ou um Optional vazio.
     */
    @Transactional
    public Optional<CategoriaEntity> getByID(Long id) {
        return repository.findById(id);
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
}
