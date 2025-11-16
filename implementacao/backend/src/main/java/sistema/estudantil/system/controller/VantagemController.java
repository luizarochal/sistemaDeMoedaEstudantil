package sistema.estudantil.system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;
import sistema.estudantil.system.service.VantagemService;
import sistema.estudantil.system.models.Vantagem;
import sistema.estudantil.system.dtos.VantagemDTO; // ← Adicione esta importação
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/vantagens")
@CrossOrigin(origins = "*") // Ou "http://localhost:5173" se souber a porta do front
@Tag(name = "Vantagens", description = "Operações para gerenciamento de vantagens oferecidas pelas empresas")
public class VantagemController {

    @Autowired
    private VantagemService vantagemService;

    @Operation(summary = "Criar vantagem", description = "Cadastra uma nova vantagem associada a uma empresa parceira")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vantagem criada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos"),
            @ApiResponse(responseCode = "404", description = "Empresa não encontrada"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @PostMapping(value = "/empresa/{cnpj}", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<Vantagem> createVantagem(@PathVariable String cnpj,
            @RequestPart("vantagem") Vantagem vantagem,
            @RequestPart(value = "file", required = false) MultipartFile file) {
        try {
            return ResponseEntity.ok(vantagemService.createVantagem(cnpj, vantagem, file));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Buscar vantagem por ID", description = "Retorna uma vantagem específica pelo seu ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vantagem encontrada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Vantagem não encontrada"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping("/{id}")
    public ResponseEntity<VantagemDTO> getVantagemById(@PathVariable @NonNull Long id) {
        return vantagemService.getVantagemById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Listar vantagens por empresa", description = "Retorna todas as vantagens oferecidas por uma empresa específica")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de vantagens retornada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Empresa não encontrada"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping("/empresa/{cnpj}")
    public ResponseEntity<List<VantagemDTO>> getVantagensByEmpresa(@PathVariable String cnpj) {
        return ResponseEntity.ok(vantagemService.getVantagensByEmpresa(cnpj));
    }

    @Operation(summary = "Listar todas as vantagens", description = "Retorna todas as vantagens disponíveis no sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de vantagens retornada com sucesso"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping
    public ResponseEntity<List<VantagemDTO>> getAllVantagens() {
        return ResponseEntity.ok(vantagemService.getAllVantagens());
    }

    @Operation(summary = "Atualizar vantagem", description = "Atualiza os dados de uma vantagem existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vantagem atualizada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Vantagem não encontrada"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @PutMapping(value = "/{id}", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<Vantagem> updateVantagem(@PathVariable @NonNull Long id,
            @RequestPart("vantagem") Vantagem vantagemDetails,
            @RequestPart(value = "file", required = false) MultipartFile file) {
        try {
            Vantagem updatedVantagem = vantagemService.updateVantagem(id, vantagemDetails, file);
            return ResponseEntity.ok(updatedVantagem);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Deletar vantagem", description = "Remove uma vantagem do sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Vantagem deletada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Vantagem não encontrada"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVantagem(@PathVariable @NonNull Long id) {
        try {
            vantagemService.deleteVantagem(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}