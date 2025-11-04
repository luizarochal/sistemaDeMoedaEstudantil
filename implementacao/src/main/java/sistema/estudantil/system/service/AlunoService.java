package sistema.estudantil.system.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sistema.estudantil.system.models.Aluno;
import sistema.estudantil.system.repositories.AlunoRepository;

import java.util.List;
import java.util.Optional;

@Service
public class AlunoService {
    
    @Autowired
    private AlunoRepository alunoRepository;

    public List<Aluno> findAll() {
        return alunoRepository.findAll();
    }

    public Optional<Aluno> findById(Long id) {
        return alunoRepository.findById(id);
    }

    public Optional<Aluno> findByEmail(String email) {
        return alunoRepository.findByEmail(email);
    }

    @Transactional
    public Aluno save(Aluno aluno) {
        if (alunoRepository.existsByEmail(aluno.getEmail())) {
            throw new RuntimeException("Já existe um aluno com este email");
        }
        
        return alunoRepository.save(aluno);
    }

    @Transactional
    public Aluno update(Long id, Aluno alunoDetails) {
        Aluno aluno = alunoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Aluno não encontrado com id: " + id));

        aluno.setNome(alunoDetails.getNome());
        aluno.setEmail(alunoDetails.getEmail());
        aluno.setInstituicao(alunoDetails.getInstituicao());
        aluno.setCurso(alunoDetails.getCurso());
        aluno.setQuantidadeMoedas(alunoDetails.getQuantidadeMoedas());

        return alunoRepository.save(aluno);
    }

    @Transactional
    public void delete(Long id) {
        Aluno aluno = alunoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Aluno não encontrado com id: " + id));
        alunoRepository.delete(aluno);
    }

    @Transactional
    public Aluno adicionarMoedas(Long id, float moedas) {
        Aluno aluno = alunoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Aluno não encontrado com id: " + id));
        
        float novaQuantidade = aluno.getQuantidadeMoedas() + moedas;
        aluno.setQuantidadeMoedas(novaQuantidade);
        
        return alunoRepository.save(aluno);
    }

    @Transactional
    public Aluno removerMoedas(Long id, float moedas) {
        Aluno aluno = alunoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Aluno não encontrado com id: " + id));
        
        if (aluno.getQuantidadeMoedas() < moedas) {
            throw new RuntimeException("Saldo insuficiente de moedas");
        }
        
        float novaQuantidade = aluno.getQuantidadeMoedas() - moedas;
        aluno.setQuantidadeMoedas(novaQuantidade);
        
        return alunoRepository.save(aluno);
    }
}