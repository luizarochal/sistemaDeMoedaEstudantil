package sistema.estudantil.system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;
import sistema.estudantil.system.service.VantagemService;
import sistema.estudantil.system.models.Vantagem;
import sistema.estudantil.system.dtos.VantagemDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/vantagens")
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
    public ResponseEntity<?> createVantagem(@PathVariable String cnpj,
            @RequestPart("vantagem") Vantagem vantagem,
            @RequestPart(value = "file", required = false) MultipartFile file) {
        try {
            return ResponseEntity.ok(vantagemService.createVantagem(cnpj, vantagem, file));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (IOException e) {
            return ResponseEntity.badRequest().body("Erro ao processar imagem: " + e.getMessage());
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
    public ResponseEntity<?> updateVantagem(@PathVariable @NonNull Long id,
            @RequestPart("vantagem") Vantagem vantagemDetails,
            @RequestPart(value = "file", required = false) MultipartFile file) {
        try {
            Vantagem updatedVantagem = vantagemService.updateVantagem(id, vantagemDetails, file);
            return ResponseEntity.ok(updatedVantagem);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (IOException e) {
            return ResponseEntity.badRequest().body("Erro ao processar imagem: " + e.getMessage());
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

    @Operation(summary = "Obter imagem da vantagem", description = "Retorna a imagem associada a uma vantagem")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Imagem retornada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Vantagem ou imagem não encontrada")
    })
    @SuppressWarnings("null")

    @GetMapping("/{id}/imagem")
    public ResponseEntity<byte[]> getImagemVantagem(@PathVariable Long id) {
        Optional<Vantagem> opt = vantagemService.getVantagemEntityById(id);

        if (opt.isPresent()) {
            Vantagem vantagem = opt.get();
            System.out.println("DEBUG - Processando imagem para vantagem ID: " + id);
            System.out.println("DEBUG - Dados imagem: "
                    + (vantagem.getDadosImagem() != null ? vantagem.getDadosImagem().length + " bytes" : "null"));
            System.out.println("DEBUG - Tipo MIME: " + vantagem.getTipoMime());

            if (vantagem.getDadosImagem() != null && vantagem.getTipoMime() != null) {
                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(vantagem.getTipoMime()))
                        .header("Cache-Control", "max-age=3600")
                        .body(vantagem.getDadosImagem());
            } else {
                System.out.println("DEBUG - Vantagem sem imagem ou tipo MIME");
            }
        } else {
            System.out.println("DEBUG - Vantagem não encontrada: " + id);
        }

        return ResponseEntity.notFound().build();
    }

}