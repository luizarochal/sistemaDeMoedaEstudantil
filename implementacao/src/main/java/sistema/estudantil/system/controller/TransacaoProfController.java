package sistema.estudantil.system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sistema.estudantil.system.service.TransacaoProfService;
import sistema.estudantil.system.models.TransacaoProf;
import java.util.List;

@RestController
@RequestMapping("/transacoesProf")
public class TransacaoProfController {

    @Autowired
    private TransacaoProfService transacaoProfService;

    @PostMapping
    public ResponseEntity<TransacaoProf> realizarTransacao(@RequestBody TransacaoProf transacaoProf) {
        TransacaoProf novaTransacao = transacaoProfService.realizarTransacao(transacaoProf);
        return ResponseEntity.ok(novaTransacao);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransacaoProf> obterTransacaoPorId(@PathVariable Long id) {
        TransacaoProf transacaoProf = transacaoProfService.obterTransacaoPorId(id);
        return ResponseEntity.ok(transacaoProf);
    }

    @GetMapping
    public ResponseEntity<List<TransacaoProf>> listarTodasTransacoes() {
        List<TransacaoProf> transacoes = transacaoProfService.listarTodasTransacoes();
        return ResponseEntity.ok(transacoes);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarTransacao(@PathVariable Long id) {
        transacaoProfService.deletarTransacao(id);
        return ResponseEntity.noContent().build();
    }
}
