package sistema.estudantil.system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sistema.estudantil.system.service.TransacaoProfService;
import sistema.estudantil.system.models.TransacaoProf;
import sistema.estudantil.system.dtos.TransacaoRequestDTO;
import sistema.estudantil.system.dtos.TransacaoProfDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.lang.NonNull;

import java.util.List;

@RestController
@RequestMapping("/api/transacoes-prof")
@Tag(name = "Transações Professor", description = "Operações para gerenciamento de transações de moedas entre professores e alunos")
public class TransacaoProfController {

    @Autowired
    private TransacaoProfService transacaoProfService;

    @Operation(summary = "Realizar transação", description = "Realiza uma transação de moedas de um professor para um aluno, com envio de email de notificação")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Transação realizada com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos"),
        @ApiResponse(responseCode = "404", description = "Professor ou aluno não encontrados"),
        @ApiResponse(responseCode = "402", description = "Saldo insuficiente de moedas do professor"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })

    @SuppressWarnings("null")
    @PostMapping
    public ResponseEntity<TransacaoProf> realizarTransacao(@RequestBody TransacaoRequestDTO request) {
        TransacaoProf transacao = transacaoProfService.realizarTransacao(
            request.getProfessorId(), 
            request.getAlunoId(), 
            request.getQuantidadeMoedas(), 
            request.getMensagem()
        );
        return ResponseEntity.ok(transacao);
    }

    @Operation(summary = "Obter transação por ID", description = "Retorna uma transação específica pelo seu ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Transação encontrada com sucesso"),
        @ApiResponse(responseCode = "404", description = "Transação não encontrada"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping("/{id}")
    public ResponseEntity<TransacaoProf> obterTransacaoPorId(@PathVariable @NonNull Long id) {
        TransacaoProf transacaoProf = transacaoProfService.obterTransacaoPorId(id);
        return ResponseEntity.ok(transacaoProf);
    }

    @Operation(summary = "Listar todas as transações", description = "Retorna todas as transações do sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de transações retornada com sucesso"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping
    public ResponseEntity<List<TransacaoProfDTO>> listarTodasTransacoes() {
        List<TransacaoProfDTO> transacoes = transacaoProfService.listarTodasTransacoesDTO();
        return ResponseEntity.ok(transacoes);
    }
    

    @Operation(summary = "Listar transações por professor", description = "Retorna todas as transações realizadas por um professor específico")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de transações retornada com sucesso"),
        @ApiResponse(responseCode = "404", description = "Professor não encontrado"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping("/professor/{professorId}")
    public ResponseEntity<List<TransacaoProfDTO>> listarTransacoesPorProfessor(@PathVariable Long professorId) {
        List<TransacaoProfDTO> transacoes = transacaoProfService.listarTransacoesPorProfessorDTO(professorId);
        return ResponseEntity.ok(transacoes);
    }

    @Operation(summary = "Listar transações por aluno", description = "Retorna todas as transações recebidas por um aluno específico")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de transações retornada com sucesso"),
        @ApiResponse(responseCode = "404", description = "Aluno não encontrado"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping("/aluno/{alunoId}")
    public ResponseEntity<List<TransacaoProfDTO>> listarTransacoesPorAluno(@PathVariable Long alunoId) {
        List<TransacaoProfDTO> transacoes = transacaoProfService.listarTransacoesPorAlunoDTO(alunoId);
        return ResponseEntity.ok(transacoes);
    }

    @Operation(summary = "Deletar transação", description = "Remove uma transação do sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Transação deletada com sucesso"),
        @ApiResponse(responseCode = "404", description = "Transação não encontrada"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarTransacao(@PathVariable @NonNull Long id) {
        transacaoProfService.deletarTransacao(id);
        return ResponseEntity.noContent().build();
    }
}