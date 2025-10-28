package sistema.estudantil.system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import sistema.estudantil.system.service.VantagemService;

import sistema.estudantil.system.models.Vantagem;

import java.util.List;

@RestController
@RequestMapping("/api/vantagens")
public class VantagemController {

    @Autowired
    private VantagemService vantagemService;

    // CREATE
    // Cria uma vantagem associada a uma empresa
    @PostMapping("/empresa/{cnpj}")
    public ResponseEntity<Vantagem> createVantagem(@PathVariable String cnpj, @RequestBody Vantagem vantagem) {
        try {
            return ResponseEntity.ok(vantagemService.createVantagem(cnpj, vantagem));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // READ (One)
    @GetMapping("/{id}")
    public ResponseEntity<Vantagem> getVantagemById(@PathVariable Long id) {
        return vantagemService.getVantagemById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // READ (All by Empresa)
    @GetMapping("/empresa/{cnpj}")
    public ResponseEntity<List<Vantagem>> getVantagensByEmpresa(@PathVariable String cnpj) {
        return ResponseEntity.ok(vantagemService.getVantagensByEmpresa(cnpj));
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<Vantagem> updateVantagem(@PathVariable Long id, @RequestBody Vantagem vantagemDetails) {
        try {
            return ResponseEntity.ok(vantagemService.updateVantagem(id, vantagemDetails));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVantagem(@PathVariable Long id) {
        try {
            vantagemService.deleteVantagem(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}