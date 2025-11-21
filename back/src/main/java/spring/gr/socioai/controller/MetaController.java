package spring.gr.socioai.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import spring.gr.socioai.controller.http.requests.MetaDTO;
import spring.gr.socioai.controller.http.responses.MetaResponse;
import spring.gr.socioai.service.MetaService;

import java.util.List;

@RestController
@RequestMapping("/api/metas")
public class MetaController {

    private final MetaService service;

    /**
     * Construtor para injeção de dependência do serviço.
     *
     * @param service O serviço de Metas.
     */
    @Autowired
    public MetaController(MetaService service) {
        this.service = service;
    }

    /**
     * Endpoint para salvar uma nova Meta.
     * Mapeado para HTTP POST /api/metas
     *
     * @param metasDTO DTO contendo os dados da nova Meta.
     * @return ResponseEntity com a Meta salva e status 201 (CREATED).
     */
    @PostMapping
    public ResponseEntity<MetaResponse> save(@Valid @RequestBody MetaDTO metasDTO) {
        var savedMeta = service.save(metasDTO);
        return new ResponseEntity<>(savedMeta, HttpStatus.CREATED);
    }

    /**
     * Endpoint para salvar uma lista de Metas em lote.
     * Mapeado para HTTP POST /api/metas/batch
     *
     * @param metasDTO Lista de DTOs das Metas a serem salvas.
     * @return ResponseEntity com a lista de Metas salvas e status 201 (CREATED).
     */
    @PostMapping("/batch")
    public ResponseEntity<List<MetaResponse>> saveAll(@Valid @RequestBody List<MetaDTO> metasDTO) {
        var savedMetas = service.saveAll(metasDTO);
        return new ResponseEntity<>(savedMetas, HttpStatus.CREATED);
    }

    /**
     * Endpoint para atualizar uma Meta existente.
     * Mapeado para HTTP PUT /api/metas/{id}
     *
     * @param id       O ID da Meta a ser atualizada.
     * @param metasDTO DTO com os novos dados.
     * @return ResponseEntity com a Meta atualizada e status 200 (OK),
     * ou status 404 (NOT FOUND) se o ID não existir.
     */
    @PutMapping("/{id}")
    public ResponseEntity<MetaResponse> update(@PathVariable Long id, @Valid @RequestBody MetaDTO metasDTO) {
        var updatedMeta = service.update(id, metasDTO);
        return ResponseEntity.ok(updatedMeta);
    }

    /**
     * Endpoint para retornar todas as Metas.
     * Mapeado para HTTP GET /api/metas
     *
     * @return ResponseEntity com a lista de todas as Metas e status 200 (OK).
     */
    @GetMapping
    public ResponseEntity<List<MetaResponse>> getAll() {
        var metas = service.getAll();
        return ResponseEntity.ok(metas);
    }

    /**
     * Endpoint para buscar uma Meta pelo seu ID.
     * Mapeado para HTTP GET /api/metas/{id}
     *
     * @param id O ID da Meta a ser buscada.
     * @return ResponseEntity com a Meta e status 200 (OK),
     * ou status 404 (NOT FOUND) se a Meta não for encontrada.
     */
    @GetMapping("/{id}")
    public ResponseEntity<MetaResponse> getByID(@PathVariable Long id) {
        return ResponseEntity.ok(service.getByID(id));
    }

    /**
     * Endpoint para deletar uma Meta pelo seu ID.
     * Mapeado para HTTP DELETE /api/metas/{id}
     *
     * @param id O ID da Meta a ser deletada.
     * @return ResponseEntity com status 204 (NO CONTENT) se a deleção for bem-sucedida,
     * ou status 404 (NOT FOUND) se o ID não existir.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Endpoint para deletar uma lista de Metas com base em seus IDs.
     * Mapeado para HTTP DELETE /api/metas/batch
     *
     * @param ids Lista de IDs (Long) das Metas a serem deletadas, enviada no corpo da requisição.
     * @return ResponseEntity com status 204 (NO CONTENT) se a deleção for bem-sucedida,
     * ou status 400 (BAD REQUEST) se a lista de IDs estiver vazia.
     */
    @DeleteMapping("/batch")
    public ResponseEntity<Void> deleteAll(@RequestBody List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        service.deleteAll(ids);
        return ResponseEntity.noContent().build();
    }

    /**
     * Endpoint para buscar todas as metas de um usuário
     *
     * @param username nome do usuário
     * @return uma lista com todos os usuários ou lança exceção caso o usuário não exista
     */
    @GetMapping("/u/{username}")
    public ResponseEntity<List<MetaResponse>> getAllByUsername(@PathVariable String username) {
        return ResponseEntity.ok(service.getAllMetasByUsername(username));
    }
}