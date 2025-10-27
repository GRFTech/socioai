package spring.gr.socioai.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import spring.gr.socioai.controller.http.requests.DespesaDTO;
import spring.gr.socioai.controller.http.responses.DespesaResponse;
import spring.gr.socioai.model.DespesaEntity;
import spring.gr.socioai.repository.DespesaRepository;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class DespesaService {


    private final DespesaRepository repository;

    /**
     * Converte um DespesasDTO em uma entidade Despesas para persistência.
     * Nota: O relacionamento {@code categoria} é inicializado como nulo neste CRUD básico.
     * Em uma aplicação real, a categoria seria buscada e setada aqui.
     *
     * @param dto O DTO contendo valor e data de criação da despesa.
     * @return A entidade Despesas pronta para ser salva.
     */
    private DespesaEntity convertToEntity(DespesaDTO dto) {
        return new DespesaEntity(
                null,
                dto.getDescricao(),
                dto.getValor(),
                dto.getDataCriacao(),
                null
        );
    }

    /**
     * Salva uma nova Despesa no banco de dados.
     *
     * @param despesasDTO DTO contendo os dados da despesa a ser salva.
     * @return A Despesa salva, incluindo o ID gerado.
     */
    @Transactional
    public DespesaResponse save(DespesaDTO despesasDTO) {
        DespesaEntity novaDespesa = convertToEntity(despesasDTO);
        return toResponse(repository.save(novaDespesa));
    }

    /**
     * Salva uma lista de novas Despesas no banco de dados.
     *
     * @param despesasDTO Lista de DTOs contendo os dados das Despesas a serem salvas.
     * @return Lista das Despesas salvas, cada uma com seu ID gerado.
     */
    @Transactional
    public List<DespesaResponse> saveAll(List<DespesaDTO> despesasDTO) {
        List<DespesaEntity> despesas = despesasDTO.stream()
                .map(this::convertToEntity)
                .toList();
        return repository.saveAll(despesas).stream().map(this::toResponse).toList();
    }

    /**
     * Atualiza uma Despesa existente com base no ID.
     *
     * @param id O ID da Despesa a ser atualizada.
     * @param despesasDTO DTO com os novos dados (valor, dataCriacao).
     * @return A Despesa atualizada.
     * @throws NoSuchElementException se a Despesa com o ID fornecido não for encontrada.
     */
    @Transactional
    public DespesaResponse update(Long id, DespesaDTO despesasDTO) {
        DespesaEntity existingDespesa = repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Despesa com ID " + id + " não encontrada para atualização."));

        existingDespesa.setValor(despesasDTO.getValor());
        existingDespesa.setDataCriacao(despesasDTO.getDataCriacao());

        return toResponse(repository.save(existingDespesa));
    }

    /**
     * Retorna todas as Despesas cadastradas no banco de dados.
     *
     * @return Uma lista de todas as Despesas.
     */
    @Transactional
    public List<DespesaResponse> getAll() {
        return repository.findAll().stream().map(this::toResponse).toList();
    }

    /**
     * Busca uma Despesa pelo seu ID.
     *
     * @param id O ID da Despesa a ser buscada.
     * @return Um Optional contendo a Despesa se encontrada, ou um Optional vazio.
     */
    @Transactional
    public DespesaResponse getByID(Long id) {

        var d = repository.findById(id).orElseThrow(() -> new NoSuchElementException("Despesa não encontrada!"));

        return toResponse(d);
    }

    /**
     * Deleta uma Despesa específica pelo seu ID.
     *
     * @param id O ID da Despesa a ser deletada.
     * @throws NoSuchElementException se a Despesa com o ID fornecido não for encontrada.
     */
    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new NoSuchElementException("Despesa com ID " + id + " não encontrada para deleção.");
        }
        repository.deleteById(id);
    }

    /**
     * Deleta uma lista de Despesas com base em seus IDs.
     *
     * @param ids Lista de IDs das Despesas a serem deletadas.
     */
    @Transactional
    public void deleteAll(List<Long> ids) {
        repository.deleteAllById(ids);
    }

    private DespesaResponse toResponse(DespesaEntity despesaEntity) {
        return new DespesaResponse(despesaEntity.getId(), despesaEntity.getDescricao(), despesaEntity.getValor(), despesaEntity.getDataCriacao(), despesaEntity.getCategoria().getId());
    }
}
