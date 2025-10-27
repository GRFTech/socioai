package spring.gr.socioai.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import spring.gr.socioai.controller.http.requests.MetaDTO;
import spring.gr.socioai.controller.http.responses.MetaResponse;
import spring.gr.socioai.model.MetaEntity;
import spring.gr.socioai.repository.MetaRepository;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class MetaService {

    private final MetaRepository repository;

    /**
     * Converte um MetasDTO em uma entidade Metas para persistência.
     * Nota: O relacionamento {@code categoria} é inicializado como nulo neste CRUD básico.
     * Em uma aplicação real, a categoria seria buscada e setada aqui.
     *
     * @param dto O DTO contendo os dados da meta.
     * @return A entidade Metas pronta para ser salva.
     */
    private MetaEntity convertToEntity(MetaDTO dto) {
        return new MetaEntity(
                null,
                dto.getDescricao(),
                dto.getValorAtual(),
                dto.getDataInicio(),
                dto.getDataFim(),
                null
        );
    }

    /**
     * Salva uma nova Meta no banco de dados.
     *
     * @param metasDTO DTO contendo os dados da meta a ser salva.
     * @return A Meta salva, incluindo o ID gerado.
     */
    @Transactional
    public MetaResponse save(MetaDTO metasDTO) {
        MetaEntity novaMeta = convertToEntity(metasDTO);
        return toResponse(repository.save(novaMeta));
    }

    /**
     * Salva uma lista de novas Metas no banco de dados.
     *
     * @param metasDTO Lista de DTOs contendo os dados das Metas a serem salvas.
     * @return Lista das Metas salvas, cada uma com seu ID gerado.
     */
    @Transactional
    public List<MetaResponse> saveAll(List<MetaDTO> metasDTO) {
        List<MetaEntity> metas = metasDTO.stream()
                .map(this::convertToEntity)
                .toList();
        return repository.saveAll(metas).stream().map(this::toResponse).toList();
    }

    /**
     * Atualiza uma Meta existente com base no ID.
     *
     * @param id O ID da Meta a ser atualizada.
     * @param metasDTO DTO com os novos dados (descricao, valorAtual, dataInicio, dataFim).
     * @return A Meta atualizada.
     * @throws NoSuchElementException se a Meta com o ID fornecido não for encontrada.
     */
    @Transactional
    public MetaResponse update(Long id, MetaDTO metasDTO) {
        MetaEntity existingMeta = repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Meta com ID " + id + " não encontrada para atualização."));

        existingMeta.setDescricao(metasDTO.getDescricao());
        existingMeta.setValorAtual(metasDTO.getValorAtual());
        existingMeta.setDataInicio(metasDTO.getDataInicio());
        existingMeta.setDataFim(metasDTO.getDataFim());

        return toResponse(repository.save(existingMeta));
    }

    /**
     * Retorna todas as Metas cadastradas no banco de dados.
     *
     * @return Uma lista de todas as Metas.
     */
    @Transactional
    public List<MetaResponse> getAll() {
        return repository.findAll().stream().map(this::toResponse).toList();
    }

    /**
     * Busca uma Meta pelo seu ID.
     *
     * @param id O ID da Meta a ser buscada.
     * @return Um Optional contendo a Meta se encontrada, ou um Optional vazio.
     */
    @Transactional
    public MetaResponse getByID(Long id) {

        var m = repository.findById(id).orElseThrow(() -> new NoSuchElementException("Meta não encontrada para id: " + id));

        return toResponse(m);
    }

    /**
     * Deleta uma Meta específica pelo seu ID.
     *
     * @param id O ID da Meta a ser deletada.
     * @throws NoSuchElementException se a Meta com o ID fornecido não for encontrada.
     */
    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new NoSuchElementException("Meta com ID " + id + " não encontrada para deleção.");
        }
        repository.deleteById(id);
    }

    /**
     * Deleta uma lista de Metas com base em seus IDs.
     *
     * @param ids Lista de IDs das Metas a serem deletadas.
     */
    @Transactional
    public void deleteAll(List<Long> ids) {
        repository.deleteAllById(ids);
    }

    private MetaResponse toResponse(MetaEntity metaEntity) {
        return new MetaResponse(metaEntity.getId(), metaEntity.getDescricao(), metaEntity.getValorAtual(), metaEntity.getDataInicio(), metaEntity.getDataFim(), metaEntity.getCategoria().getId());
    }
}
