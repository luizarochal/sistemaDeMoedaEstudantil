package sistema.estudantil.system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sistema.estudantil.system.service.EmpresaService;
import sistema.estudantil.system.models.Empresa;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@RestController
@RequestMapping("/api/empresas")
@Tag(name = "Empresas", description = "Operações para gerenciamento de empresas parceiras")
public class EmpresaController {

    @Autowired
    private EmpresaService empresaService;

    @Operation(summary = "Criar empresa", description = "Cadastra uma nova empresa parceira no sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Empresa criada com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos"),
        @ApiResponse(responseCode = "409", description = "Já existe uma empresa com este CNPJ"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @PostMapping
    public ResponseEntity<Empresa> createEmpresa(@RequestBody Empresa empresa) {
        return ResponseEntity.ok(empresaService.createEmpresa(empresa));
    }

    @Operation(summary = "Listar todas as empresas", description = "Retorna todas as empresas cadastradas no sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de empresas retornada com sucesso"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping
    public ResponseEntity<List<Empresa>> getAllEmpresas() {
        return ResponseEntity.ok(empresaService.getAllEmpresas());
    }

    @Operation(summary = "Buscar empresa por CNPJ", description = "Retorna uma empresa específica pelo seu CNPJ")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Empresa encontrada com sucesso"),
        @ApiResponse(responseCode = "404", description = "Empresa não encontrada"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping("/{cnpj}")
    public ResponseEntity<Empresa> getEmpresaByCnpj(@PathVariable String cnpj) {
        return empresaService.getEmpresaByCnpj(cnpj)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Atualizar empresa", description = "Atualiza os dados de uma empresa existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Empresa atualizada com sucesso"),
        @ApiResponse(responseCode = "404", description = "Empresa não encontrada"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @PutMapping("/{cnpj}")
    public ResponseEntity<Empresa> updateEmpresa(@PathVariable String cnpj, @RequestBody Empresa empresaDetails) {
        try {
            return ResponseEntity.ok(empresaService.updateEmpresa(cnpj, empresaDetails));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Deletar empresa", description = "Remove uma empresa do sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Empresa deletada com sucesso"),
        @ApiResponse(responseCode = "404", description = "Empresa não encontrada"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @DeleteMapping("/{cnpj}")
    public ResponseEntity<Void> deleteEmpresa(@PathVariable String cnpj) {
        try {
            empresaService.deleteEmpresa(cnpj);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}