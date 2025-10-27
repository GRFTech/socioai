package spring.gr.socioai.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import spring.gr.socioai.controller.http.requests.ReceitaDTO;
import spring.gr.socioai.controller.http.responses.ReceitaResponse;
import spring.gr.socioai.model.ReceitaEntity;
import spring.gr.socioai.repository.ReceitaRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ReceitaService {

    private final ReceitaRepository repository;

    /**
     * Converte um ReceitasDTO em uma entidade Receitas para persistência.
     * Nota: O relacionamento {@code categoria} é inicializado como nulo neste CRUD básico.
     * Em uma aplicação real, a categoria deve ser buscada e setada aqui.
     *
     * @param dto O DTO contendo valor e data de criação da receita.
     * @return A entidade Receitas pronta para ser salva.
     */
    private ReceitaEntity convertToEntity(ReceitaDTO dto) {
        return new ReceitaEntity(
                null,
                dto.getDescricao(),
                dto.getValor(),
                dto.getDataCriacao(),
                null
        );
    }

    /**
     * Salva uma nova Receita no banco de dados.
     *
     * @param receitasDTO DTO contendo os dados da receita a ser salva.
     * @return A Receita salva, incluindo o ID gerado.
     */
    @Transactional
    public ReceitaResponse save(ReceitaDTO receitasDTO) {
        ReceitaEntity novaReceita = convertToEntity(receitasDTO);
        return toResponse(repository.save(novaReceita));
    }

    /**
     * Salva uma lista de novas Receitas no banco de dados.
     *
     * @param receitasDTO Lista de DTOs contendo os dados das Receitas a serem salvas.
     * @return Lista das Receitas salvas, cada uma com seu ID gerado.
     */
    @Transactional
    public List<ReceitaResponse> saveAll(List<ReceitaDTO> receitasDTO) {
        List<ReceitaEntity> receitas = receitasDTO.stream()
                .map(this::convertToEntity)
                .toList();
        return repository.saveAll(receitas).stream().map(this::toResponse).toList();
    }

    /**
     * Atualiza uma Receita existente com base no ID.
     *
     * @param id O ID da Receita a ser atualizada.
     * @param receitasDTO DTO com os novos dados (valor, dataCriacao).
     * @return A Receita atualizada.
     * @throws NoSuchElementException se a Receita com o ID fornecido não for encontrada.
     */
    @Transactional
    public ReceitaResponse update(Long id, ReceitaDTO receitasDTO) {
        ReceitaEntity existingReceita = repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Receita com ID " + id + " não encontrada para atualização."));

        existingReceita.setValor(receitasDTO.getValor());
        existingReceita.setDataCriacao(receitasDTO.getDataCriacao());

        return toResponse(repository.save(existingReceita));
    }

    /**
     * Retorna todas as Receitas cadastradas no banco de dados.
     *
     * @return Uma lista de todas as Receitas.
     */
    @Transactional
    public List<ReceitaResponse> getAll() {
        return repository.findAll().stream().map(this::toResponse).toList();
    }

    /**
     * Busca uma Receita pelo seu ID.
     *
     * @param id O ID da Receita a ser buscada.
     * @return Um Optional contendo a Receita se encontrada, ou um Optional vazio.
     */
    @Transactional
    public ReceitaResponse getByID(Long id) {

        var r = repository.findById(id).orElseThrow(() -> new NoSuchElementException("Receita não encontrada com id: " + id));

        return toResponse(r);
    }

    /**
     * Deleta uma Receita específica pelo seu ID.
     *
     * @param id O ID da Receita a ser deletada.
     * @throws NoSuchElementException se a Receita com o ID fornecido não for encontrada.
     */
    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new NoSuchElementException("Receita com ID " + id + " não encontrada para deleção.");
        }
        repository.deleteById(id);
    }

    /**
     * Deleta uma lista de Receitas com base em seus IDs.
     *
     * @param ids Lista de IDs das Receitas a serem deletadas.
     */
    @Transactional
    public void deleteAll(List<Long> ids) {
        repository.deleteAllById(ids);
    }

    private ReceitaResponse toResponse(ReceitaEntity entity) {
        return new ReceitaResponse(entity.getId(), entity.getDescricao(), entity.getValor(), entity.getDataCriacao(), entity.getCategoria().getId());
    }
}
