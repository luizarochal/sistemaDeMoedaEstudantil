package sistema.estudantil.system.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sistema.estudantil.system.repositories.VantagemRepository;
import sistema.estudantil.system.repositories.EmpresaRepository;
import sistema.estudantil.system.models.Vantagem;
import sistema.estudantil.system.models.Empresa;
import java.util.List;
import java.util.Optional;

@Service
public class VantagemService {

    @Autowired
    private VantagemRepository vantagemRepository;

    @Autowired
    private EmpresaRepository empresaRepository;

    // CREATE (Implementa o "cadastrar()" do diagrama)
    @Transactional
    public Vantagem createVantagem(String empresaCnpj, Vantagem vantagem) {
        Empresa empresa = empresaRepository.findByCnpj(empresaCnpj)
                .orElseThrow(() -> new RuntimeException("Empresa não encontrada com CNPJ: " + empresaCnpj));
        
        vantagem.setEmpresaDono(empresa);
        
        return vantagemRepository.save(vantagem);
    }

    // READ (One)
    @Transactional(readOnly = true)
    public Optional<Vantagem> getVantagemById(Long id) {
        return vantagemRepository.findById(id);
    }

    // READ (All by Empresa)
    @Transactional(readOnly = true)
    public List<Vantagem> getVantagensByEmpresa(String cnpj) {
        return vantagemRepository.findByEmpresaDonoCnpj(cnpj);
    }

    // UPDATE
    @Transactional
    public Vantagem updateVantagem(Long id, Vantagem vantagemDetails) {
        Vantagem vantagem = vantagemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vantagem não encontrada com ID: " + id));

        vantagem.setNome(vantagemDetails.getNome());
        vantagem.setDescricao(vantagemDetails.getDescricao());
        vantagem.setCusto(vantagemDetails.getCusto());
        vantagem.setFoto(vantagemDetails.getFoto());
        vantagem.setCupom(vantagemDetails.getCupom());

        return vantagemRepository.save(vantagem);
    }

    // DELETE
    @Transactional
    public void deleteVantagem(Long id) {
        if (!vantagemRepository.existsById(id)) {
            throw new RuntimeException("Vantagem não encontrada com ID: " + id);
        }
        vantagemRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<Vantagem> getAllVantagens() {
        return vantagemRepository.findAll();
    }
}