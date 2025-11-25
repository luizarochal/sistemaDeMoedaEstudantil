package sistema.estudantil.system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sistema.estudantil.system.service.TransacaoResgateService;
import sistema.estudantil.system.dtos.TransacaoResgateDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@RestController
@RequestMapping("/api/transacoes-resgate")
@Tag(name = "Transações de Resgate", description = "Operações para gerenciamento de transações de resgate de vantagens")
public class TransacaoResgateController {

    @Autowired
    private TransacaoResgateService transacaoResgateService;

    @Operation(summary = "Criar transação de resgate", description = "Registra uma nova transação de resgate de vantagem")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Transação criada com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @PostMapping
    public ResponseEntity<Void> criarTransacaoResgate(@RequestBody TransacaoResgateDTO transacaoResgateDTO) {
        transacaoResgateService.registrarTransacaoResgate(
            transacaoResgateDTO.getAlunoId(),
            transacaoResgateDTO.getAlunoNome(),
            transacaoResgateDTO.getVantagemId(),
            transacaoResgateDTO.getVantagemNome(),
            transacaoResgateDTO.getCustoMoedas(),
            transacaoResgateDTO.getCodigoCupom()
        );
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Listar transações de resgate por aluno", description = "Retorna todas as transações de resgate realizadas por um aluno específico")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de transações retornada com sucesso"),
        @ApiResponse(responseCode = "404", description = "Aluno não encontrado"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping("/aluno/{alunoId}")
    public ResponseEntity<List<TransacaoResgateDTO>> listarTransacoesResgatePorAluno(@PathVariable Long alunoId) {
        List<TransacaoResgateDTO> transacoes = transacaoResgateService.listarTransacoesResgatePorAluno(alunoId);
        return ResponseEntity.ok(transacoes);
    }

    @Operation(summary = "Listar todas as transações de resgate", description = "Retorna todas as transações de resgate do sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de transações retornada com sucesso"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping
    public ResponseEntity<List<TransacaoResgateDTO>> listarTodasTransacoesResgate() {
        List<TransacaoResgateDTO> transacoes = transacaoResgateService.listarTodasTransacoesResgate();
        return ResponseEntity.ok(transacoes);
    }
}