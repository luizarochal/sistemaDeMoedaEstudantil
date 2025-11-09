package sistema.estudantil.system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sistema.estudantil.system.service.AlunoService;
import sistema.estudantil.system.models.Aluno;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.lang.NonNull;

import java.util.Optional;

@RestController
@RequestMapping("/api/alunos")
@Tag(name = "Alunos", description = "Operações para gerenciamento de alunos")
public class AlunoController {
    
    @Autowired
    private AlunoService alunoService;

    @Operation(summary = "Criar aluno", description = "Cadastra um novo aluno no sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Aluno criado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos"),
        @ApiResponse(responseCode = "409", description = "Já existe um aluno com este email"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @PostMapping
    public ResponseEntity<Aluno> createAluno(@RequestBody Aluno aluno) {
        Aluno createdAluno = alunoService.save(aluno);
        return ResponseEntity.ok(createdAluno);
    }

    @Operation(summary = "Buscar aluno por ID", description = "Retorna um aluno específico pelo seu ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Aluno encontrado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Aluno não encontrado"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Optional<Aluno>> getAlunoById(@PathVariable @NonNull Long id) {
        Optional<Aluno> aluno = alunoService.findById(id);
        return ResponseEntity.ok(aluno);
    }

    @Operation(summary = "Atualizar aluno", description = "Atualiza os dados de um aluno existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Aluno atualizado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Aluno não encontrado"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Aluno> updateAluno(@PathVariable @NonNull Long id, @RequestBody Aluno alunoDetails) {
        Aluno updatedAluno = alunoService.update(id, alunoDetails);
        return ResponseEntity.ok(updatedAluno);
    }

    @Operation(summary = "Deletar aluno", description = "Remove um aluno do sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Aluno deletado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Aluno não encontrado"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAluno(@PathVariable @NonNull Long id) {
        alunoService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Listar todos os alunos", description = "Retorna todos os alunos cadastrados no sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de alunos retornada com sucesso"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping
    public ResponseEntity<java.util.List<Aluno>> getAllAlunos() {
        java.util.List<Aluno> alunos = alunoService.findAll();
        return ResponseEntity.ok(alunos);
    }
}