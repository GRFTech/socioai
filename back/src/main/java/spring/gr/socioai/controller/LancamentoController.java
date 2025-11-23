package spring.gr.socioai.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import spring.gr.socioai.controller.http.requests.LancamentoDTO;
import spring.gr.socioai.controller.http.responses.LancamentoResponse;
import spring.gr.socioai.controller.http.responses.PivotFinanceiroResponse;
import spring.gr.socioai.service.LancamentoService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/lancamentos")
public class LancamentoController {

    private final LancamentoService service;

    /**
     * Endpoint para salvar um novo Lançamento.
     * Mapeado para HTTP POST /api/lancamentos
     *
     * @param lancamentoDTO DTO contendo os dados do novo Lançamento.
     * @return ResponseEntity com o Lançamento salvo e status 201 (CREATED).
     */
    @PostMapping
    public ResponseEntity<LancamentoResponse> save(@Valid @RequestBody LancamentoDTO lancamentoDTO) {
        var savedLancamento = service.save(lancamentoDTO);
        return new ResponseEntity<>(savedLancamento, HttpStatus.CREATED);
    }

    /**
     * Endpoint para salvar uma lista de Lançamentos em lote.
     * Mapeado para HTTP POST /api/lancamentos/batch
     *
     * @param lancamentoDTO Lista de DTOs dos Lançamentos a serem salvos.
     * @return ResponseEntity com a lista de Lançamentos salvos e status 201 (CREATED).
     */
    @PostMapping("/batch")
    public ResponseEntity<List<LancamentoResponse>> saveAll(@Valid @RequestBody List<LancamentoDTO> lancamentoDTO) {
        var savedLancamentos = service.saveAll(lancamentoDTO);
        return new ResponseEntity<>(savedLancamentos, HttpStatus.CREATED);
    }

    /**
     * Endpoint para atualizar um Lançamento existente.
     * Mapeado para HTTP PUT /api/lancamentos/{id}
     *
     * @param id O ID do Lançamento a ser atualizado.
     * @param lancamentoDTO DTO com os novos dados.
     * @return ResponseEntity com o Lançamento atualizado e status 200 (OK),
     * ou status 404 (NOT FOUND) se o ID não existir.
     */
    @PutMapping("/{id}")
    public ResponseEntity<LancamentoResponse> update(@PathVariable Long id, @Valid @RequestBody LancamentoDTO lancamentoDTO) {
        var updatedLancamento = service.update(id, lancamentoDTO);
        return ResponseEntity.ok(updatedLancamento);
    }

    /**
     * Endpoint para retornar todos os Lançamentos.
     * Mapeado para HTTP GET /api/lancamentos
     *
     * @return ResponseEntity com a lista de todos os Lançamentos e status 200 (OK).
     */
    @GetMapping
    public ResponseEntity<List<LancamentoResponse>> getAll() {
        var lancamentos = service.getAll();
        return ResponseEntity.ok(lancamentos);
    }

    /**
     * Endpoint para buscar um Lançamento pelo seu ID.
     * Mapeado para HTTP GET /api/lancamentos/{id}
     *
     * @param id O ID do Lançamento a ser buscado.
     * @return ResponseEntity com o Lançamento e status 200 (OK),
     * ou status 404 (NOT FOUND) se o Lançamento não for encontrado.
     */
    @GetMapping("/{id}")
    public ResponseEntity<LancamentoResponse> getByID(@PathVariable Long id) {
        return ResponseEntity.ok(service.getByID(id));
    }

    /**
     * Endpoint para deletar um Lançamento pelo seu ID.
     * Mapeado para HTTP DELETE /api/lancamentos/{id}
     *
     * @param id O ID do Lançamento a ser deletado.
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
     * Endpoint para deletar uma lista de Lançamentos com base em seus IDs.
     * Mapeado para HTTP DELETE /api/lancamentos/batch
     *
     * @param ids Lista de IDs (Long) dos Lançamentos a serem deletados, enviada no corpo da requisição.
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
     * Endpoint responsável por pegar todos os lançamentos de um usuário
     *
     * @param username nome do usuário
     * @return retorna uma lista de lançamentos ou lança uma exceção dizendo que o usuário não existe
     */
    @GetMapping("/u/{username}")
    public ResponseEntity<List<LancamentoResponse>> getAllLancamentosByUsername(@PathVariable String username) {
        return ResponseEntity.ok(service.getAllLancamentosByUsername(username));
    }

    @GetMapping("/fluxo-caixa/historico/{username}")
    public ResponseEntity<List<PivotFinanceiroResponse>> getHistoricoCompleto(@PathVariable String username) {
        var resultado = service.gerarPivotCompleto(username);
        return ResponseEntity.ok(resultado);
    }

    @GetMapping("/fluxo-caixa/periodo/{username}")
    public ResponseEntity<List<PivotFinanceiroResponse>> getPorPeriodo(
            @PathVariable String username,
            @RequestParam("inicio") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam("fim") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim) {

        LocalDateTime dataInicio = inicio.atStartOfDay(); // 00:00:00
        LocalDateTime dataFim = fim.atTime(LocalTime.MAX); // 23:59:59.999999

        var resultado = service.gerarPivotPorPeriodo(username, dataInicio, dataFim);
        return ResponseEntity.ok(resultado);
    }

    @GetMapping("/fluxo-caixa/global")
    public ResponseEntity<List<PivotFinanceiroResponse>> getFluxoCaixaGlobal() {
        var resultado = service.gerarPivotGlobal();
        return ResponseEntity.ok(resultado);
    }

}
