package sistema.estudantil.system.service;

import org.springframework.beans.factory.annotation.Autowired;

import jakarta.transaction.Transactional;
import sistema.estudantil.system.models.TransacaoProf;
import sistema.estudantil.system.models.Aluno;
import sistema.estudantil.system.models.Professor;
import sistema.estudantil.system.repositories.TransacaoProfRepository;

import java.util.List;

public class TransacaoProfService {
    @Autowired
    private ProfessorService professorService;
    @Autowired
    private TransacaoProfRepository transacaoProfRepository;
    @Autowired
    private AlunoService alunoService;

    @Transactional
    public TransacaoProf realizarTransacao(TransacaoProf transacaoProf) {
        Professor professor = professorService.buscarProfessorPorId(transacaoProf.getProfessor().getId())
                .orElseThrow(() -> new RuntimeException("Professor não encontrado com ID: " + transacaoProf.getProfessor().getId()));

        Aluno aluno = alunoService.findById(transacaoProf.getAluno().getId())
                .orElseThrow(() -> new RuntimeException("Aluno não encontrado com ID: " + transacaoProf.getAluno().getId()));

        TransacaoProf transacao = new TransacaoProf();
        transacao.setProfessor(professor);
        transacao.setQuantidadeMoedas(transacaoProf.getQuantidadeMoedas());
        transacao.setMensagem(transacaoProf.getMensagem());
        transacao.setAluno(aluno);

        return transacaoProfRepository.save(transacao);
    }

    public List<TransacaoProf> listarTodasTransacoes() {
        return transacaoProfRepository.findAll();
    }

    public TransacaoProf obterTransacaoPorId(Long id) {
        return transacaoProfRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transação não encontrada com ID: " + id));
    }

    @Transactional
    public void deletarTransacao(Long id) {
        transacaoProfRepository.deleteById(id);
    }

    public TransacaoProf criarTransacao(TransacaoProf transacaoProf) {
        return transacaoProfRepository.save(transacaoProf);
    }

}