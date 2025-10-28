package sistema.estudantil.system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import sistema.estudantil.system.service.EmpresaService;

import sistema.estudantil.system.models.Empresa;



import java.util.List;

@RestController
@RequestMapping("/api/empresas")
public class EmpresaController {

    @Autowired
    private EmpresaService empresaService;

    // CREATE
    @PostMapping
    public ResponseEntity<Empresa> createEmpresa(@RequestBody Empresa empresa) {
        return ResponseEntity.ok(empresaService.createEmpresa(empresa));
    }

    // READ (All)
    @GetMapping
    public ResponseEntity<List<Empresa>> getAllEmpresas() {
        return ResponseEntity.ok(empresaService.getAllEmpresas());
    }

    // READ (One)
    @GetMapping("/{cnpj}")
    public ResponseEntity<Empresa> getEmpresaByCnpj(@PathVariable String cnpj) {
        return empresaService.getEmpresaByCnpj(cnpj)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // UPDATE
    @PutMapping("/{cnpj}")
    public ResponseEntity<Empresa> updateEmpresa(@PathVariable String cnpj, @RequestBody Empresa empresaDetails) {
        try {
            return ResponseEntity.ok(empresaService.updateEmpresa(cnpj, empresaDetails));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // DELETE
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