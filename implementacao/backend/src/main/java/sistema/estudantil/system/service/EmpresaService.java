package sistema.estudantil.system.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sistema.estudantil.system.repositories.EmpresaRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import sistema.estudantil.system.dtos.EmpresaDTO;
import sistema.estudantil.system.models.Empresa;

@Service
public class EmpresaService {

    @Autowired
    private EmpresaRepository empresaRepository;

    // CREATE
    @Transactional
    public Empresa createEmpresa(@NonNull Empresa empresa) {
        return empresaRepository.save(empresa);
    }

    // READ (One)
    @Transactional(readOnly = true)
    public Optional<Empresa> getEmpresaByCnpj(@NonNull String cnpj) {
        return empresaRepository.findByCnpj(cnpj);
    }

    // READ (All)
    @Transactional(readOnly = true)
    public List<Empresa> getAllEmpresas() {
        return empresaRepository.findAll();
    }

    // UPDATE
    @SuppressWarnings("null")
    @Transactional
    public Empresa updateEmpresa(@NonNull String cnpj, @NonNull Empresa empresaDetails) {
        Empresa empresa = empresaRepository.findByCnpj(cnpj)
                .orElseThrow(() -> new RuntimeException("Empresa não encontrada com CNPJ: " + cnpj));

        if (empresaDetails.getNome() != null) {
            empresa.setNome(empresaDetails.getNome());
        }
        if (empresaDetails.getEndereco() != null) {
            empresa.setEndereco(empresaDetails.getEndereco());
        }
        if (empresaDetails.getPassword() != null) {
            empresa.setPassword(empresaDetails.getPassword());
        }
        if (empresaDetails.getEmail() != null) {
            empresa.setEmail(empresaDetails.getEmail());
        }

        return empresaRepository.save(empresa);
    }

    // DELETE
    @Transactional
    public void deleteEmpresa(@NonNull String cnpj) {
        if (!empresaRepository.existsByCnpj(cnpj)) {
            throw new RuntimeException("Empresa não encontrada com CNPJ: " + cnpj);
        }
        empresaRepository.deleteByCnpj(cnpj);
    }

    private EmpresaDTO toDTO(Empresa empresa) {
        return new EmpresaDTO(
                empresa.getNome(),
                empresa.getEmail(),
                empresa.getCnpj(),
                empresa.getEndereco());
    }

    @Transactional(readOnly = true)
    public List<EmpresaDTO> getAllEmpresasDTO() {
        List<Empresa> empresas = empresaRepository.findAll();
        return empresas.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<EmpresaDTO> getEmpresaByCnpjDTO(String cnpj) {
        return empresaRepository.findByCnpj(cnpj)
                .map(this::toDTO);
    }
}