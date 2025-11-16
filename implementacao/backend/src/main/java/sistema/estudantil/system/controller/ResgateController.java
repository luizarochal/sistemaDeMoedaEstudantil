package sistema.estudantil.system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sistema.estudantil.system.service.ResgateService;
import sistema.estudantil.system.models.Resgate;
import sistema.estudantil.system.dtos.ResgateRequestDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.lang.NonNull;
import java.util.List;

@RestController
@RequestMapping("/api/resgates")
@Tag(name = "Resgates", description = "Operações para gerenciamento de resgates de vantagens")
public class ResgateController {

    @Autowired
    private ResgateService resgateService;

    @Operation(summary = "Resgatar vantagem", description = "Realiza o resgate de uma vantagem por um aluno, gerando um cupom e enviando emails de confirmação")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Vantagem resgatada com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos"),
        @ApiResponse(responseCode = "404", description = "Aluno ou vantagem não encontrados"),
        @ApiResponse(responseCode = "402", description = "Saldo insuficiente de moedas"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })

    @SuppressWarnings("null")
    @PostMapping
    public ResponseEntity<Resgate> resgatarVantagem(@RequestBody ResgateRequestDTO request) {
        Resgate resgate = resgateService.resgatarVantagem(request.getAlunoId(), request.getVantagemId());
        return ResponseEntity.ok(resgate);
    }

    @Operation(summary = "Listar resgates por aluno", description = "Retorna todos os resgates realizados por um aluno específico")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de resgates retornada com sucesso"),
        @ApiResponse(responseCode = "404", description = "Aluno não encontrado"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping("/aluno/{alunoId}")
    public ResponseEntity<List<Resgate>> listarResgatesPorAluno(@PathVariable Long alunoId) {
        List<Resgate> resgates = resgateService.listarResgatesPorAluno(alunoId);
        return ResponseEntity.ok(resgates);
    }

    @Operation(summary = "Listar resgates por empresa", description = "Retorna todos os resgates de vantagens de uma empresa específica")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de resgates retornada com sucesso"),
        @ApiResponse(responseCode = "404", description = "Empresa não encontrada"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping("/empresa/{cnpj}")
    public ResponseEntity<List<Resgate>> listarResgatesPorEmpresa(@PathVariable String cnpj) {
        List<Resgate> resgates = resgateService.listarResgatesPorEmpresa(cnpj);
        return ResponseEntity.ok(resgates);
    }

    @Operation(summary = "Obter resgate por ID", description = "Retorna um resgate específico pelo seu ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Resgate encontrado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Resgate não encontrado"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Resgate> obterResgatePorId(@PathVariable @NonNull Long id) {
        Resgate resgate = resgateService.obterResgatePorId(id);
        return ResponseEntity.ok(resgate);
    }

    @Operation(summary = "Marcar cupom como utilizado", description = "Marca um cupom de resgate como utilizado pelo parceiro")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Cupom marcado como utilizado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Resgate não encontrado"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @PutMapping("/{id}/utilizar")
    public ResponseEntity<Void> marcarCupomComoUtilizado(@PathVariable @NonNull Long id) {
        resgateService.marcarCupomComoUtilizado(id);
        return ResponseEntity.noContent().build();
    }
}