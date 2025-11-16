package sistema.estudantil.system.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sistema.estudantil.system.repositories.VantagemRepository;
import sistema.estudantil.system.repositories.EmpresaRepository;
import sistema.estudantil.system.models.Vantagem;
import sistema.estudantil.system.models.Empresa;
import sistema.estudantil.system.dtos.VantagemDTO; // ← Adicione esta importação

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.lang.NonNull;

@Service
public class VantagemService {

    @Autowired
    private VantagemRepository vantagemRepository;

    @Autowired
    private EmpresaRepository empresaRepository;

    // CREATE
    @Transactional
    public Vantagem createVantagem(String empresaCnpj, Vantagem vantagem) {
        Empresa empresa = empresaRepository.findByCnpj(empresaCnpj)
                .orElseThrow(() -> new RuntimeException("Empresa não encontrada com CNPJ: " + empresaCnpj));

        vantagem.setEmpresaDono(empresa);
        return vantagemRepository.save(vantagem);
    }

    // READ (One) - Retorna DTO
    @Transactional(readOnly = true)
    public Optional<VantagemDTO> getVantagemById(@NonNull Long id) {
        return vantagemRepository.findById(id)
                .map(this::toDTO);
    }

    // READ (All by Empresa) - Retorna DTOs
    @Transactional(readOnly = true)
    public List<VantagemDTO> getVantagensByEmpresa(String cnpj) {
        List<Vantagem> vantagens = vantagemRepository.findByEmpresaDonoCnpj(cnpj);
        return vantagens.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // READ (All) - Retorna DTOs
    @Transactional(readOnly = true)
    public List<VantagemDTO> getAllVantagens() {
        List<Vantagem> vantagens = vantagemRepository.findAll();
        return vantagens.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // Método auxiliar para converter Vantagem para VantagemDTO
    private VantagemDTO toDTO(Vantagem vantagem) {
        return new VantagemDTO(
            vantagem.getIdVantagem(),
            vantagem.getNome(),
            vantagem.getDescricao(),
            vantagem.getCusto(),
            vantagem.getFoto(),
            vantagem.getCupom(),
            vantagem.getEmpresaDono() != null ? vantagem.getEmpresaDono().getNome() : null,
            vantagem.getEmpresaDono() != null ? vantagem.getEmpresaDono().getCnpj() : null
        );
    }

    // UPDATE (mantém a entidade para update)
    @Transactional
    public Vantagem updateVantagem(@NonNull Long id, Vantagem vantagemDetails) {
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
    public void deleteVantagem(@NonNull Long id) {
        if (!vantagemRepository.existsById(id)) {
            throw new RuntimeException("Vantagem não encontrada com ID: " + id);
        }
        vantagemRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Optional<Vantagem> getVantagemEntityById(@NonNull Long id) {
        return vantagemRepository.findById(id);
    }

    // MÉTODO NOVO: Para obter todas as vantagens como entidades
    @Transactional(readOnly = true)
    public List<Vantagem> getAllVantagensEntities() {
        return vantagemRepository.findAll();
    }

    // MÉTODO NOVO: Para obter vantagens por empresa como entidades
    @Transactional(readOnly = true)
    public List<Vantagem> getVantagensByEmpresaEntities(String cnpj) {
        return vantagemRepository.findByEmpresaDonoCnpj(cnpj);
    }
}