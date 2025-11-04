package sistema.estudantil.system.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sistema.estudantil.system.repositories.EmpresaRepository;
import java.util.List;
import java.util.Optional;
import sistema.estudantil.system.models.Empresa;


@Service
public class EmpresaService {

    @Autowired
    private EmpresaRepository empresaRepository;

    // CREATE
    @Transactional
    public Empresa createEmpresa(Empresa empresa) {
        // Adicionar validação de CNPJ aqui, se necessário
        return empresaRepository.save(empresa);
    }

    // READ (One)
    @Transactional(readOnly = true)
    public Optional<Empresa> getEmpresaByCnpj(String cnpj) {
        return empresaRepository.findByCnpj(cnpj);
    }

    // READ (All)
    @Transactional(readOnly = true)
    public List<Empresa> getAllEmpresas() {
        return empresaRepository.findAll();
    }

    // UPDATE
    @Transactional
    public Empresa updateEmpresa(String cnpj, Empresa empresaDetails) {
        Empresa empresa = empresaRepository.findByCnpj(cnpj)
                .orElseThrow(() -> new RuntimeException("Empresa não encontrada com CNPJ: " + cnpj));

        if(empresaDetails.getNome() != null){
            empresa.setNome(empresaDetails.getNome());
        }
        if(empresaDetails.getEndereco() != null){
            empresa.setEndereco(empresaDetails.getEndereco());
        }
        if(empresaDetails.getPassword() != null){
            empresa.setPassword(empresaDetails.getPassword());
        }
        if(empresaDetails.getEmail() != null){
            empresa.setEmail(empresaDetails.getEmail());
        }

        
        return empresaRepository.save(empresa);
    }

    // DELETE
    @Transactional
    public void deleteEmpresa(String cnpj) {
        if (!empresaRepository.existsByCnpj(cnpj)) {
            throw new RuntimeException("Empresa não encontrada com CNPJ: " + cnpj);
        }
        empresaRepository.deleteByCnpj(cnpj);
    }
}