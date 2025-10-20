package spring.gr.socioai.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import spring.gr.socioai.controller.http.requests.DespesaDTO;
import spring.gr.socioai.model.DespesaEntity;
import spring.gr.socioai.repository.DespesaRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

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
        // Usa o construtor AllArgsConstructor (com id=null e categoria=null)
        return new DespesaEntity(
                null,
                dto.getDescricao(),
                dto.getValor(),
                dto.getDataCriacao(),
                null // categoria (ManyToOne) - deve ser preenchida com lógica de negócio real
        );
    }

    /**
     * Salva uma nova Despesa no banco de dados.
     *
     * @param despesasDTO DTO contendo os dados da despesa a ser salva.
     * @return A Despesa salva, incluindo o ID gerado.
     */
    @Transactional
    public DespesaEntity save(DespesaDTO despesasDTO) {
        DespesaEntity novaDespesa = convertToEntity(despesasDTO);
        return repository.save(novaDespesa);
    }

    /**
     * Salva uma lista de novas Despesas no banco de dados.
     *
     * @param despesasDTO Lista de DTOs contendo os dados das Despesas a serem salvas.
     * @return Lista das Despesas salvas, cada uma com seu ID gerado.
     */
    @Transactional
    public List<DespesaEntity> saveAll(List<DespesaDTO> despesasDTO) {
        List<DespesaEntity> despesas = despesasDTO.stream()
                .map(this::convertToEntity)
                .toList();
        return repository.saveAll(despesas);
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
    public DespesaEntity update(Long id, DespesaDTO despesasDTO) {
        DespesaEntity existingDespesa = repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Despesa com ID " + id + " não encontrada para atualização."));

        existingDespesa.setValor(despesasDTO.getValor());
        existingDespesa.setDataCriacao(despesasDTO.getDataCriacao());

        return repository.save(existingDespesa);
    }

    /**
     * Retorna todas as Despesas cadastradas no banco de dados.
     *
     * @return Uma lista de todas as Despesas.
     */
    @Transactional
    public List<DespesaEntity> getAll() {
        return repository.findAll();
    }

    /**
     * Busca uma Despesa pelo seu ID.
     *
     * @param id O ID da Despesa a ser buscada.
     * @return Um Optional contendo a Despesa se encontrada, ou um Optional vazio.
     */
    @Transactional
    public Optional<DespesaEntity> getByID(Long id) {
        return repository.findById(id);
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
}
