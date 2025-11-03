package spring.gr.socioai.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import spring.gr.socioai.controller.http.requests.LancamentoDTO;
import spring.gr.socioai.controller.http.responses.LancamentoResponse;
import spring.gr.socioai.model.LancamentoEntity;
import spring.gr.socioai.model.valueobjects.TipoLancamento;
import spring.gr.socioai.repository.CategoriaMicroRepository;
import spring.gr.socioai.repository.LancamentoRepository;

import java.util.List;
import java.util.NoSuchElementException;

@Repository
@RequiredArgsConstructor
public class LancamentoService {

    private final LancamentoRepository repository;
    private final CategoriaMicroRepository categoriaMicroRepository;
    private final MetaService metaService;

    /**
     * Converte um LancamentoDTO em uma entidade Lancamento para persistência.
     * Nota: O relacionamento {@code categoria} é inicializado como nulo neste CRUD básico.
     *
     * @param dto O DTO contendo valor e data de criação do Lancamento.
     * @return A entidade Lancamento pronta para ser salva.
     */
    private LancamentoEntity convertToEntity(LancamentoDTO dto) {
        return new LancamentoEntity(
                null,
                dto.getDescricao(),
                dto.getValor(),
                TipoLancamento.valueOf(dto.getTipoLancamento()),
                dto.getDataCriacao(),
                categoriaMicroRepository.getReferenceById(dto.getMicroCategoriaId())
        );
    }

    /**
     * Salva uma nova Receita no banco de dados.
     *
     * @param lancamentoDTO DTO contendo os dados da receita a ser salva.
     * @return A Receita salva, incluindo o ID gerado.
     */
    @Transactional
    public LancamentoResponse save(LancamentoDTO lancamentoDTO) {
        LancamentoEntity novoLancamento = convertToEntity(lancamentoDTO);

        var microMeta = categoriaMicroRepository.getReferenceById(lancamentoDTO.getMicroCategoriaId());

        metaService.atualizaMeta(microMeta.getMeta().getId(), lancamentoDTO);

        return toResponse(repository.save(novoLancamento));
    }

    /**
     * Salva uma lista de novas Receitas no banco de dados.
     *
     * @param lancamentoDTO Lista de DTOs contendo os dados das Receitas a serem salvas.
     * @return Lista das Receitas salvas, cada uma com seu ID gerado.
     */
    @Transactional
    public List<LancamentoResponse> saveAll(List<LancamentoDTO> lancamentoDTO) {
        List<LancamentoEntity> receitas = lancamentoDTO.stream()
                .map(this::convertToEntity)
                .toList();
        return repository.saveAll(receitas).stream().map(this::toResponse).toList();
    }

    /**
     * Atualiza uma Receita existente com base no ID.
     *
     * @param id O ID da Receita a ser atualizada.
     * @param lancamentoDTO DTO com os novos dados (valor, dataCriacao).
     * @return A Receita atualizada.
     * @throws NoSuchElementException se a Receita com o ID fornecido não for encontrada.
     */
    @Transactional
    public LancamentoResponse update(Long id, LancamentoDTO lancamentoDTO) {
        LancamentoEntity existingReceita = repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Receita com ID " + id + " não encontrada para atualização."));

        existingReceita.setValor(lancamentoDTO.getValor());
        existingReceita.setDataCriacao(lancamentoDTO.getDataCriacao());

        return toResponse(repository.save(existingReceita));
    }

    /**
     * Retorna todas as Receitas cadastradas no banco de dados.
     *
     * @return Uma lista de todas as Receitas.
     */
    @Transactional
    public List<LancamentoResponse> getAll() {
        return repository.findAll().stream().map(this::toResponse).toList();
    }

    /**
     * Busca uma Receita pelo seu ID.
     *
     * @param id O ID da Receita a ser buscada.
     * @return Um Optional contendo a Receita se encontrada, ou um Optional vazio.
     */
    @Transactional
    public LancamentoResponse getByID(Long id) {

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

    private LancamentoResponse toResponse(LancamentoEntity entity) {
        return new LancamentoResponse(
                entity.getId(),
                entity.getDescricao(),
                entity.getValor(),
                entity.getTipoLancamento().name(),
                entity.getDataCriacao(),
                entity.getCategoriaMicro().getId()
        );
    }
}
