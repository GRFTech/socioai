package spring.gr.socioai.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import spring.gr.socioai.controller.http.requests.LancamentoDTO;
import spring.gr.socioai.controller.http.requests.MetaDTO;
import spring.gr.socioai.controller.http.responses.MetaResponse;
import spring.gr.socioai.model.LancamentoEntity;
import spring.gr.socioai.model.MetaEntity;
import spring.gr.socioai.model.valueobjects.Email;
import spring.gr.socioai.repository.AuthenticatedUserRepository;
import spring.gr.socioai.repository.CategoriaRepository;
import spring.gr.socioai.repository.MetaRepository;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class MetaService {

    private final MetaRepository repository;
    private final CategoriaRepository categoriaRepository;
    private final AuthenticatedUserRepository authenticatedUserRepository;

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
                categoriaRepository.getReferenceById(dto.getCategoria()),
                null
        );
    }

    /**
     * Salva uma nova Meta no banco de dados.
     *
     * @param metaDTO DTO contendo os dados da meta a ser salva.
     * @return A Meta salva, incluindo o ID gerado.
     */
    @Transactional
    @PreAuthorize("hasPermission(#metaDTO.categoria, 'categoria', 'WRITE')")
    public MetaResponse save(MetaDTO metaDTO) {
        MetaEntity novaMeta = convertToEntity(metaDTO);
        var meta = repository.save(novaMeta);
        return toResponse(meta);
    }

    /**
     * Salva uma lista de novas Metas no banco de dados.
     *
     * @param metasDTO Lista de DTOs contendo os dados das Metas a serem salvas.
     * @return Lista das Metas salvas, cada uma com seu ID gerado.
     */
    @Transactional
    @PreAuthorize("hasRole('ROLE_ADMIN')")
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
    @PreAuthorize("hasPermission(#id, 'meta', 'WRITE')")
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
    @PreAuthorize("hasRole('ROLE_ADMIN')")
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
    @PreAuthorize("hasRole('ROLE_ADMIN')")
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
    @PreAuthorize("hasPermission(#id, 'meta', 'DELETE')")
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new NoSuchElementException("Meta com ID " + id + " não encontrada para remoção");
        }
        repository.deleteById(id);
    }

    /**
     * Deleta uma lista de Metas com base em seus IDs.
     *
     * @param ids Lista de IDs das Metas a serem deletadas.
     */
    @Transactional
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void deleteAll(List<Long> ids) {
        repository.deleteAllById(ids);
    }

    /**
     * Metodo responsável por buscar todas as metas de um usuário
     *
     * @param username o nome do usuário
     * @return uma lista com todas as metas dele ou lança uma exception dizendo que o usuário não existe
     */
    @Transactional
    @PreAuthorize("#username == authentication.name or hasRole('ROLE_ADMIN')")
    public List<MetaResponse> getAllMetasByUsername(String username) {

        if(!authenticatedUserRepository.existsByUsername(new Email(username))){
            throw new EntityNotFoundException("Usuário com username " + username + " não encontrado");
        }

        return repository
                .getAllByCategoria_User_Username(new Email(username))
                .stream().map(this::toResponse)
                .toList();
    }

    private MetaResponse toResponse(MetaEntity metaEntity) {
        return new MetaResponse(
                metaEntity.getId(),
                metaEntity.getDescricao(),
                metaEntity.getValorAtual(),
                metaEntity.getDataInicio(),
                metaEntity.getDataFim(),
                metaEntity.getCategoria().getId(),
                metaEntity.getLancamentos() == null ? null : metaEntity.getLancamentos().stream().map(LancamentoEntity::getId).toList()
        );
    }

    public void atualizaMeta(Long id, LancamentoDTO lancamentoDTO) {
        var meta = repository.getReferenceById(id);

        meta.setValorAtual(meta.getValorAtual() + lancamentoDTO.getValor());
        repository.save(meta);
    }
}
