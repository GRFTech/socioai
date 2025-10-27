package spring.gr.socioai.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import spring.gr.socioai.controller.http.requests.ReceitaDTO;
import spring.gr.socioai.controller.http.responses.ReceitaResponse;
import spring.gr.socioai.service.ReceitaService;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/receitas")
public class ReceitaController {

    private final ReceitaService service;

    /**
     * Endpoint para salvar uma nova Receita.
     * Mapeado para HTTP POST /api/receitas
     *
     * @param receitasDTO DTO contendo os dados da nova Receita.
     * @return ResponseEntity com a Receita salva e status 201 (CREATED).
     */
    @PostMapping
    public ResponseEntity<ReceitaResponse> save(@Valid @RequestBody ReceitaDTO receitasDTO) {
        var savedReceita = service.save(receitasDTO);
        return new ResponseEntity<>(savedReceita, HttpStatus.CREATED);
    }

    /**
     * Endpoint para salvar uma lista de Receitas em lote.
     * Mapeado para HTTP POST /api/receitas/batch
     *
     * @param receitasDTO Lista de DTOs das Receitas a serem salvas.
     * @return ResponseEntity com a lista de Receitas salvas e status 201 (CREATED).
     */
    @PostMapping("/batch")
    public ResponseEntity<List<ReceitaResponse>> saveAll(@Valid @RequestBody List<ReceitaDTO> receitasDTO) {
        var savedReceitas = service.saveAll(receitasDTO);
        return new ResponseEntity<>(savedReceitas, HttpStatus.CREATED);
    }

    /**
     * Endpoint para atualizar uma Receita existente.
     * Mapeado para HTTP PUT /api/receitas/{id}
     *
     * @param id O ID da Receita a ser atualizada.
     * @param receitasDTO DTO com os novos dados.
     * @return ResponseEntity com a Receita atualizada e status 200 (OK),
     * ou status 404 (NOT FOUND) se o ID não existir.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ReceitaResponse> update(@PathVariable Long id, @Valid @RequestBody ReceitaDTO receitasDTO) {
        try {
            var updatedReceita = service.update(id, receitasDTO);
            return ResponseEntity.ok(updatedReceita);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Endpoint para retornar todas as Receitas.
     * Mapeado para HTTP GET /api/receitas
     *
     * @return ResponseEntity com a lista de todas as Receitas e status 200 (OK).
     */
    @GetMapping
    public ResponseEntity<List<ReceitaResponse>> getAll() {
        var receitas = service.getAll();
        return ResponseEntity.ok(receitas);
    }

    /**
     * Endpoint para buscar uma Receita pelo seu ID.
     * Mapeado para HTTP GET /api/receitas/{id}
     *
     * @param id O ID da Receita a ser buscada.
     * @return ResponseEntity com a Receita e status 200 (OK),
     * ou status 404 (NOT FOUND) se a Receita não for encontrada.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ReceitaResponse> getByID(@PathVariable Long id) {
        return ResponseEntity.ok(service.getByID(id));
    }

    /**
     * Endpoint para deletar uma Receita pelo seu ID.
     * Mapeado para HTTP DELETE /api/receitas/{id}
     *
     * @param id O ID da Receita a ser deletada.
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
     * Endpoint para deletar uma lista de Receitas com base em seus IDs.
     * Mapeado para HTTP DELETE /api/receitas/batch
     *
     * @param ids Lista de IDs (Long) das Receitas a serem deletadas, enviada no corpo da requisição.
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