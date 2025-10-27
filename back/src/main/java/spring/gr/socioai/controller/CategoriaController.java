package spring.gr.socioai.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import spring.gr.socioai.controller.http.requests.CategoriaDTO;
import spring.gr.socioai.controller.http.responses.CategoriaResponse;
import spring.gr.socioai.model.CategoriaEntity;
import spring.gr.socioai.service.CategoriaService;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/categorias")
public class CategoriaController {

    private final CategoriaService service;

    /**
     * Endpoint para salvar uma nova Categoria.
     * Mapeado para HTTP POST /api/categorias
     *
     * @param categoriaDTO DTO contendo nome e tipo da nova Categoria.
     * @return ResponseEntity com a Categoria salva e status 201 (CREATED).
     */
    @PostMapping
    public ResponseEntity<CategoriaResponse> save(@Valid @RequestBody CategoriaDTO categoriaDTO) {
        var savedCategoria = service.save(categoriaDTO);
        return new ResponseEntity<>(savedCategoria, HttpStatus.CREATED);
    }

    /**
     * Endpoint para salvar uma lista de Categorias em lote.
     * Mapeado para HTTP POST /api/categorias/batch
     *
     * @param categoriasDTO Lista de DTOs das Categorias a serem salvas.
     * @return ResponseEntity com a lista de Categorias salvas e status 201 (CREATED).
     */
    @PostMapping("/batch")
    public ResponseEntity<List<CategoriaResponse>> saveAll(@Valid @RequestBody List<CategoriaDTO> categoriasDTO) {
        var savedCategorias = service.saveAll(categoriasDTO);
        return new ResponseEntity<>(savedCategorias, HttpStatus.CREATED);
    }

    /**
     * Endpoint para atualizar uma Categoria existente.
     * Mapeado para HTTP PUT /api/categorias/{id}
     *
     * @param id O ID da Categoria a ser atualizada.
     * @param categoriaDTO DTO com os novos dados.
     * @return ResponseEntity com a Categoria atualizada e status 200 (OK),
     * ou status 404 (NOT FOUND) se o ID não existir.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CategoriaResponse> update(@PathVariable Long id, @Valid @RequestBody CategoriaDTO categoriaDTO) {
        try {
            var updatedCategoria = service.update(id, categoriaDTO);
            return ResponseEntity.ok(updatedCategoria);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Endpoint para retornar todas as Categorias.
     * Mapeado para HTTP GET /api/categorias
     *
     * @return ResponseEntity com a lista de todas as Categorias e status 200 (OK).
     */
    @GetMapping
    public ResponseEntity<List<CategoriaResponse>> getAll() {
        var categorias = service.getAll();
        return ResponseEntity.ok(categorias);
    }

    /**
     * Endpoint para buscar uma Categoria pelo seu ID.
     * Mapeado para HTTP GET /api/categorias/{id}
     *
     * @param id O ID da Categoria a ser buscada.
     * @return ResponseEntity com a Categoria e status 200 (OK),
     * ou status 404 (NOT FOUND) se a Categoria não for encontrada.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CategoriaResponse> getByID(@PathVariable Long id) {
        return ResponseEntity.ok(this.service.getByID(id));
    }

    /**
     * Endpoint para deletar uma Categoria pelo seu ID.
     * Mapeado para HTTP DELETE /api/categorias/{id}
     *
     * @param id O ID da Categoria a ser deletada.
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
     * Endpoint para deletar uma lista de Categorias com base em seus IDs.
     * Mapeado para HTTP DELETE /api/categorias/batch
     *
     * @param ids Lista de IDs (Long) das Categorias a serem deletadas, enviada no corpo da requisição.
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