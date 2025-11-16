package sistema.estudantil.system.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sistema.estudantil.system.models.TransacaoProf;
import sistema.estudantil.system.dtos.TransacaoProfDTO;
import sistema.estudantil.system.models.Aluno;
import sistema.estudantil.system.models.Professor;
import sistema.estudantil.system.repositories.TransacaoProfRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransacaoProfService {

    @Autowired
    private ProfessorService professorService;

    @Autowired
    private TransacaoProfRepository transacaoProfRepository;

    @Autowired
    private AlunoService alunoService;

    @Autowired
    private EmailService emailService;

    @Transactional
    public TransacaoProf realizarTransacao(@NonNull Long professorId, @NonNull Long alunoId, int quantidadeMoedas, String mensagem) {
        Professor professor = professorService.buscarProfessorPorId(professorId)
                .orElseThrow(() -> new RuntimeException("Professor não encontrado com ID: " + professorId));

        Aluno aluno = alunoService.findById(alunoId)
                .orElseThrow(() -> new RuntimeException("Aluno não encontrado com ID: " + alunoId));

        // Verificar saldo do professor
        if (professor.getQuantidadeMoedas() < quantidadeMoedas) {
            throw new RuntimeException("Saldo insuficiente. Professor possui " + professor.getQuantidadeMoedas()
                    + " moedas, mas tentou enviar " + quantidadeMoedas);
        }

        // Criar transação
        TransacaoProf transacao = new TransacaoProf();
        transacao.setProfessor(professor);
        transacao.setAluno(aluno);
        transacao.setQuantidadeMoedas(quantidadeMoedas);
        transacao.setMensagem(mensagem);

        // Atualizar saldos
        professorService.atualizarMoedas(professorId, -quantidadeMoedas);
        alunoService.adicionarMoedas(alunoId, quantidadeMoedas);

        // Salvar transação
        TransacaoProf transacaoSalva = transacaoProfRepository.save(transacao);

        // Enviar email de notificação
        emailService.enviarEmailNotificacaoMoeda(aluno.getEmail(), professor.getNome(), quantidadeMoedas, mensagem);

        return transacaoSalva;
    }

    public List<TransacaoProfDTO> listarTodasTransacoesDTO() {
        List<TransacaoProf> transacoes = transacaoProfRepository.findAll();
        return transacoes.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }


    public TransacaoProf obterTransacaoPorId(@NonNull Long id) {
        return transacaoProfRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transação não encontrada com ID: " + id));
    }

    public List<TransacaoProfDTO> listarTransacoesPorProfessorDTO(Long professorId) {
        List<TransacaoProf> transacoes = transacaoProfRepository.findByProfessorId(professorId);
        return transacoes.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<TransacaoProfDTO> listarTransacoesPorAlunoDTO(Long alunoId) {
        List<TransacaoProf> transacoes = transacaoProfRepository.findByAlunoId(alunoId);
        return transacoes.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deletarTransacao(@NonNull Long id) {
        transacaoProfRepository.deleteById(id);
    }

    private TransacaoProfDTO toDTO(TransacaoProf transacao) {
        return new TransacaoProfDTO(
            transacao.getId(),
            transacao.getProfessor().getId(),
            transacao.getProfessor().getNome(),
            transacao.getAluno().getId(),
            transacao.getAluno().getNome(),
            transacao.getQuantidadeMoedas(),
            transacao.getMensagem(),
            transacao.getDataTransacao()
        );
    }
}