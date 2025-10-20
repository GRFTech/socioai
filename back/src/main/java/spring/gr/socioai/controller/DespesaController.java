package spring.gr.socioai.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import spring.gr.socioai.controller.http.requests.DespesaDTO;
import spring.gr.socioai.model.DespesaEntity;
import spring.gr.socioai.service.DespesaService;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/despesas")
public class DespesaController {

    private final DespesaService service;

    /**
     * Endpoint para salvar uma nova Despesa.
     * Mapeado para HTTP POST /api/despesas
     *
     * @param despesasDTO DTO contendo os dados da nova Despesa.
     * @return ResponseEntity com a Despesa salva e status 201 (CREATED).
     */
    @PostMapping
    public ResponseEntity<DespesaEntity> save(@Valid @RequestBody DespesaDTO despesasDTO) {
        DespesaEntity savedDespesa = service.save(despesasDTO);
        return new ResponseEntity<>(savedDespesa, HttpStatus.CREATED);
    }

    /**
     * Endpoint para salvar uma lista de Despesas em lote.
     * Mapeado para HTTP POST /api/despesas/batch
     *
     * @param despesasDTO Lista de DTOs das Despesas a serem salvas.
     * @return ResponseEntity com a lista de Despesas salvas e status 201 (CREATED).
     */
    @PostMapping("/batch")
    public ResponseEntity<List<DespesaEntity>> saveAll(@Valid @RequestBody List<DespesaDTO> despesasDTO) {
        List<DespesaEntity> savedDespesas = service.saveAll(despesasDTO);
        return new ResponseEntity<>(savedDespesas, HttpStatus.CREATED);
    }

    /**
     * Endpoint para atualizar uma Despesa existente.
     * Mapeado para HTTP PUT /api/despesas/{id}
     *
     * @param id O ID da Despesa a ser atualizada.
     * @param despesasDTO DTO com os novos dados.
     * @return ResponseEntity com a Despesa atualizada e status 200 (OK),
     * ou status 404 (NOT FOUND) se o ID não existir.
     */
    @PutMapping("/{id}")
    public ResponseEntity<DespesaEntity> update(@PathVariable Long id, @Valid @RequestBody DespesaDTO despesasDTO) {
        try {
            DespesaEntity updatedDespesa = service.update(id, despesasDTO);
            return ResponseEntity.ok(updatedDespesa);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Endpoint para retornar todas as Despesas.
     * Mapeado para HTTP GET /api/despesas
     *
     * @return ResponseEntity com a lista de todas as Despesas e status 200 (OK).
     */
    @GetMapping
    public ResponseEntity<List<DespesaEntity>> getAll() {
        List<DespesaEntity> despesas = service.getAll();
        return ResponseEntity.ok(despesas);
    }

    /**
     * Endpoint para buscar uma Despesa pelo seu ID.
     * Mapeado para HTTP GET /api/despesas/{id}
     *
     * @param id O ID da Despesa a ser buscada.
     * @return ResponseEntity com a Despesa e status 200 (OK),
     * ou status 404 (NOT FOUND) se a Despesa não for encontrada.
     */
    @GetMapping("/{id}")
    public ResponseEntity<DespesaEntity> getByID(@PathVariable Long id) {
        return service.getByID(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Endpoint para deletar uma Despesa pelo seu ID.
     * Mapeado para HTTP DELETE /api/despesas/{id}
     *
     * @param id O ID da Despesa a ser deletada.
     * @return ResponseEntity com status 204 (NO CONTENT) se a deleção for bem-sucedida,
     * ou status 404 (NOT FOUND) se o ID não existir.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            service.delete(id);
            return ResponseEntity.noContent().build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Endpoint para deletar uma lista de Despesas com base em seus IDs.
     * Mapeado para HTTP DELETE /api/despesas/batch
     *
     * @param ids Lista de IDs (Long) das Despesas a serem deletadas, enviada no corpo da requisição.
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
}