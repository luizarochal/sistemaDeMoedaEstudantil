package sistema.estudantil.system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sistema.estudantil.system.service.ProfessorService;
import sistema.estudantil.system.service.ProfessorTxtService;
import sistema.estudantil.system.models.Professor;
import org.springframework.lang.NonNull;
import org.springframework.security.crypto.password.PasswordEncoder;

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

    @Autowired
    private ProfessorTxtService professorTxtService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Operation(summary = "Buscar professor por ID", description = "Retorna um professor específico pelo seu ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Professor encontrado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Professor não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Professor> obterProfessorPorId(@PathVariable @NonNull Long id) {
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
    public ResponseEntity<Void> deletarProfessor(@PathVariable @NonNull Long id) {
        professorService.deletarProfessor(id);
        return ResponseEntity.noContent().build();
    }

    // Adicione este método no ProfessorController.java

    @Operation(summary = "Processar arquivo TXT", description = "Processa o arquivo TXT de professores manualmente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Arquivo processado com sucesso"),
            @ApiResponse(responseCode = "500", description = "Erro ao processar arquivo")
    })
    @PostMapping("/processar-txt")
    public ResponseEntity<String> processarArquivoTxt() {
        try {
            String resultado = professorTxtService.processarArquivoManualmente();
            return ResponseEntity.ok(resultado);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro: " + e.getMessage());
        }
    }

    @Operation(summary = "Criar professor", description = "Cadastra um novo professor no sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Professor criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos"),
            @ApiResponse(responseCode = "409", description = "Já existe um professor com este CPF"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @PostMapping
    public ResponseEntity<Professor> criarProfessor(@RequestBody Professor professor) {
        try {
            // 🔑 Criptografar a senha antes de salvar
            if (professor.getPassword() != null && !professor.getPassword().startsWith("$2a$")) {
                String senhaCriptografada = passwordEncoder.encode(professor.getPassword());
                professor.setPassword(senhaCriptografada);
            }

            Professor novoProfessor = professorService.salvarProfessor(professor);
            return ResponseEntity.ok(novoProfessor);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}