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
import jakarta.mail.MessagingException;

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
    public TransacaoProf realizarTransacao(@NonNull Long professorId, @NonNull Long alunoId, int quantidadeMoedas,
            String mensagem) {
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
        try {
            emailService.enviarEmailHtml(aluno.getEmail(), "Você recebeu moedas!",
                    gerarHtmlEmailNotificacao(professor.getNome(), quantidadeMoedas, mensagem));
        } catch (MessagingException e) {
            throw new RuntimeException("Erro ao enviar email de notificação", e);
        }

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
                transacao.getDataTransacao());
    }

    private String gerarHtmlEmailNotificacao(String nomeProfessor, int quantidadeMoedas, String mensagem) {
        return "<!DOCTYPE html>" +
                "<html lang='pt-BR'>" +
                "<head>" +
                "<meta charset='UTF-8'>" +
                "<meta name='viewport' content='width=device-width, initial-scale=1.0'>" +
                "<title>Você Recebeu Moedas!</title>" +
                "<style>" +
                "body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; margin: 0; padding: 0; background-color: #f8f9fa; }"
                +
                ".container { max-width: 600px; margin: 0 auto; background-color: white; border-radius: 12px; box-shadow: 0 4px 6px rgba(0,0,0,0.1); overflow: hidden; }"
                +
                ".header { background: linear-gradient(135deg, #10b981, #059669); color: white; padding: 30px 20px; text-align: center; }"
                +
                ".header h1 { margin: 0; font-size: 28px; font-weight: bold; }" +
                ".content { padding: 30px; }" +
                ".moedas-section { background: linear-gradient(135deg, #f59e0b, #d97706); color: white; border-radius: 8px; padding: 25px; text-align: center; margin: 20px 0; }"
                +
                ".moedas-quantidade { font-size: 42px; font-weight: bold; margin: 10px 0; }" +
                ".moedas-label { font-size: 16px; opacity: 0.9; }" +
                ".info-card { background-color: #f0f9ff; border: 1px solid #bae6fd; border-radius: 8px; padding: 20px; margin-bottom: 20px; }"
                +
                ".mensagem-card { background-color: #faf5ff; border: 1px solid #e9d5ff; border-radius: 8px; padding: 20px; margin-bottom: 20px; }"
                +
                ".card-title { color: #374151; font-size: 16px; font-weight: 600; margin-bottom: 8px; }" +
                ".card-content { color: #6b7280; line-height: 1.5; }" +
                ".professor-name { color: #7c3aed; font-weight: 600; }" +
                ".mensagem-text { font-style: italic; color: #6b7280; line-height: 1.5; }" +
                ".icon { font-size: 24px; margin-bottom: 10px; }" +
                ".footer { background-color: #f1f5f9; padding: 20px; text-align: center; color: #64748b; font-size: 12px; }"
                +
                ".coins-icon { font-size: 48px; margin-bottom: 15px; display: block; }" +
                "</style>" +
                "</head>" +
                "<body>" +
                "<div class='container'>" +
                "<div class='header'>" +
                "<div class='icon'>🎯</div>" +
                "<h1>Você recebeu moedas!</h1>" +
                "</div>" +
                "<div class='content'>" +
                "<div class='moedas-section'>" +
                "<div class='coins-icon'>🪙</div>" +
                "<div class='moedas-quantidade'>" + quantidadeMoedas + "</div>" +
                "<div class='moedas-label'>Moedas Recebidas</div>" +
                "</div>" +
                "<div class='info-card'>" +
                "<div class='card-title'>Reconhecimento do Professor</div>" +
                "<div class='card-content'>" +
                "Você recebeu <strong>" + quantidadeMoedas + " moedas</strong> do professor " +
                "<span class='professor-name'>" + nomeProfessor + "</span>" +
                "</div>" +
                "</div>" +
                "<div class='mensagem-card'>" +
                "<div class='card-title'>Mensagem do Professor</div>" +
                "<div class='mensagem-text'>"
                + (mensagem != null && !mensagem.trim().isEmpty() ? mensagem : "Nenhuma mensagem fornecida.") + "</div>"
                +
                "</div>" +
                "<p style='color: #6b7280; font-size: 14px; line-height: 1.5; text-align: center;'>" +
                "Parabéns pelo seu esforço e dedicação! Continue assim!" +
                "</p>" +
                "</div>" +
                "<div class='footer'>" +
                "<p>Sistema de Moedas Acadêmicas</p>" +
                "<p>Este é um reconhecimento pelo seu bom desempenho</p>" +
                "</div>" +
                "</div>" +
                "</body>" +
                "</html>";
    }
}