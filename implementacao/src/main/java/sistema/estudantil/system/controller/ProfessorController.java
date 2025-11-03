package sistema.estudantil.system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sistema.estudantil.system.service.ProfessorService;
import sistema.estudantil.system.models.Professor;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/professores")
@Tag(name = "Professores", description = "Operações para gerenciamento de professores")
public class ProfessorController {
    
    @Autowired
    private ProfessorService professorService;

    @Operation(summary = "Criar professor", description = "Cadastra um novo professor no sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Professor criado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos"),
        @ApiResponse(responseCode = "409", description = "Já existe um professor com este CPF"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @PostMapping
    public ResponseEntity<Professor> criarProfessor(@RequestBody Professor professor) {
        Professor novoProfessor = professorService.salvarProfessor(professor);
        return ResponseEntity.ok(novoProfessor);
    }

    @Operation(summary = "Buscar professor por ID", description = "Retorna um professor específico pelo seu ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Professor encontrado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Professor não encontrado"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Professor> obterProfessorPorId(@PathVariable Long id) {
        Optional<Professor> professor = professorService.buscarProfessorPorId(id);
        return professor.map(ResponseEntity::ok)
                        .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Listar todos os professores", description = "Retorna todos os professores cadastrados no sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de professores retornada com sucesso"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping
    public ResponseEntity<List<Professor>> listarTodosProfessores() {
        List<Professor> professores = professorService.listarTodosProfessores();
        return ResponseEntity.ok(professores);
    }

    @Operation(summary = "Deletar professor", description = "Remove um professor do sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Professor deletado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Professor não encontrado"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarProfessor(@PathVariable Long id) {
        professorService.deletarProfessor(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Consultar extrato do professor", description = "Retorna o extrato com todas as transações realizadas pelo professor")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Extrato retornado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Professor não encontrado"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping("/{id}/extrato")
    public ResponseEntity<Object> consultarExtrato(@PathVariable Long id) {
        // Implementação para retornar extrato do professor
        return ResponseEntity.ok().build();
    }
}