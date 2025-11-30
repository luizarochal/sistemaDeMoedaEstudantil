package sistema.estudantil.system.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sistema.estudantil.system.models.Resgate;
import sistema.estudantil.system.models.Aluno;
import sistema.estudantil.system.models.Vantagem;
import sistema.estudantil.system.models.Empresa;
import sistema.estudantil.system.repositories.ResgateRepository;
import sistema.estudantil.system.dtos.ResgateDTO;
import sistema.estudantil.system.dtos.TransacaoResgateDTO;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import jakarta.mail.MessagingException;

@Service
public class ResgateService {

    @Autowired
    private ResgateRepository resgateRepository;

    @Autowired
    private AlunoService alunoService;

    @Autowired
    private VantagemService vantagemService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private TransacaoResgateService transacaoResgateService; // Novo serviço

    @Transactional
    public Resgate resgatarVantagem(@NonNull Long alunoId, @NonNull Long vantagemId) {
        Aluno aluno = alunoService.findById(alunoId)
                .orElseThrow(() -> new RuntimeException("Aluno não encontrado com ID: " + alunoId));

        Vantagem vantagem = vantagemService.getVantagemEntityById(vantagemId)
                .orElseThrow(() -> new RuntimeException("Vantagem não encontrada com ID: " + vantagemId));

        // Verificar saldo do aluno
        if (aluno.getQuantidadeMoedas() < vantagem.getCusto()) {
            throw new RuntimeException("Saldo insuficiente. Aluno possui " + aluno.getQuantidadeMoedas() +
                    " moedas, mas a vantagem custa " + vantagem.getCusto());
        }

        // Gerar código do cupom
        String codigoCupom = gerarCodigoCupom();

        // Criar resgate
        Resgate resgate = new Resgate();
        resgate.setAluno(aluno);
        resgate.setVantagem(vantagem);
        resgate.setCodigoCupom(codigoCupom);

        // Atualizar saldo do aluno
        alunoService.removerMoedas(alunoId, vantagem.getCusto());

        // Salvar resgate
        Resgate resgateSalvo = resgateRepository.save(resgate);

        // Criar transação de resgate para o extrato
        transacaoResgateService.registrarTransacaoResgate(
            alunoId,
            aluno.getNome(),
            vantagemId,
            vantagem.getNome(),
            vantagem.getCusto(),
            codigoCupom
        );

        // Enviar emails
        Empresa empresa = vantagem.getEmpresaDono();

        String codigoConferencia = gerarCodigoCupom();
        try {
            emailService.enviarEmailHtml(empresa.getEmail(),
                    "Novo Resgate Realizado",
                    gerarHtmlParceiro(aluno, vantagem, codigoCupom, codigoConferencia));
            emailService.enviarEmailHtml(aluno.getEmail(),
                    "Confirmação de Resgate",
                    gerarHtmlAluno(vantagem, codigoCupom, codigoConferencia));
        } catch (MessagingException e) {
            throw new RuntimeException("Erro ao enviar emails de resgate", e);
        }

        return resgateSalvo;
    }

    // ... restante do código permanece igual
    @Transactional(readOnly = true)
    public List<ResgateDTO> listarResgatesPorAluno(Long alunoId) {
        List<Resgate> resgates = resgateRepository.findByAlunoId(alunoId);
        return resgates.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ResgateDTO> listarResgatesPorEmpresa(String cnpj) {
        List<Resgate> resgates = resgateRepository.findByVantagemEmpresaDonoCnpj(cnpj);
        return resgates.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public void marcarCupomComoUtilizado(@NonNull Long resgateId) {
        Resgate resgate = resgateRepository.findById(resgateId)
                .orElseThrow(() -> new RuntimeException("Resgate não encontrado com ID: " + resgateId));

        resgate.setUtilizado(true);
        resgateRepository.save(resgate);
    }

    @Transactional(readOnly = true)
    public ResgateDTO obterResgatePorId(@NonNull Long id) {
        Resgate resgate = resgateRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Resgate não encontrado com ID: " + id));
        return toDTO(resgate);
    }

    // Método para converter Resgate para ResgateDTO
    private ResgateDTO toDTO(Resgate resgate) {
        return new ResgateDTO(
                resgate.getId(),
                resgate.getAluno() != null ? resgate.getAluno().getId() : null,
                resgate.getAluno() != null ? resgate.getAluno().getNome() : null,
                resgate.getVantagem() != null ? resgate.getVantagem().getIdVantagem() : null,
                resgate.getVantagem() != null ? resgate.getVantagem().getNome() : null,
                resgate.getVantagem() != null ? resgate.getVantagem().getDescricao() : null,
                resgate.getVantagem() != null ? resgate.getVantagem().getCusto() : null,
                resgate.getVantagem() != null && resgate.getVantagem().getEmpresaDono() != null
                        ? resgate.getVantagem().getEmpresaDono().getNome()
                        : null,
                resgate.getCodigoCupom(),
                resgate.getDataResgate(),
                resgate.isUtilizado());
    }

    private String gerarCodigoCupom() {
        return UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    private String gerarHtmlAluno(Vantagem vantagem, String codigoCupom, String codigoConferencia) {
        return "<!DOCTYPE html>" +
                "<html lang='pt-BR'>" +
                "<head>" +
                "<meta charset='UTF-8'>" +
                "<meta name='viewport' content='width=device-width, initial-scale=1.0'>" +
                "<title>Parabéns pelo Resgate!</title>" +
                "<style>" +
                "body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; margin: 0; padding: 0; background-color: #f8f9fa; }"
                +
                ".container { max-width: 600px; margin: 0 auto; background-color: white; border-radius: 12px; box-shadow: 0 4px 6px rgba(0,0,0,0.1); overflow: hidden; }"
                +
                ".header { background: linear-gradient(135deg, #8b5cf6, #7c3aed); color: white; padding: 30px 20px; text-align: center; }"
                +
                ".header h1 { margin: 0; font-size: 28px; font-weight: bold; }" +
                ".content { padding: 30px; }" +
                ".card { background-color: #faf5ff; border: 1px solid #e9d5ff; border-radius: 8px; padding: 20px; margin-bottom: 20px; }"
                +
                ".vantagem-title { color: #7c3aed; font-size: 20px; font-weight: bold; margin-bottom: 10px; }" +
                ".vantagem-desc { color: #6b7280; line-height: 1.5; }" +
                ".cupom-section { background-color: #fef3c7; border: 1px solid #fcd34d; border-radius: 8px; padding: 20px; text-align: center; margin: 15px 0; }"
                +
                ".conferencia-section { background-color: #ecfdf5; border: 1px solid #a7f3d0; border-radius: 8px; padding: 20px; text-align: center; margin: 15px 0; }"
                +
                ".codigo { font-size: 24px; font-weight: bold; letter-spacing: 2px; margin: 10px 0; }" +
                ".codigo-cupom { color: #d97706; }" +
                ".codigo-conferencia { color: #047857; }" +
                ".info-text { color: #6b7280; font-size: 14px; line-height: 1.5; }" +
                ".footer { background-color: #f1f5f9; padding: 20px; text-align: center; color: #64748b; font-size: 12px; }"
                +
                ".icon { font-size: 24px; margin-bottom: 10px; }" +
                ".section-title { margin: 0 0 10px 0; font-weight: 500; }" +
                ".cupom-title { color: #92400e; }" +
                ".conferencia-title { color: #065f46; }" +
                "</style>" +
                "</head>" +
                "<body>" +
                "<div class='container'>" +
                "<div class='header'>" +
                "<div class='icon'>🎉</div>" +
                "<h1>Parabéns pelo seu resgate!</h1>" +
                "</div>" +
                "<div class='content'>" +
                "<div class='card'>" +
                "<div class='vantagem-title'>" + vantagem.getNome() + "</div>" +
                "<div class='vantagem-desc'>" + vantagem.getDescricao() + "</div>" +
                "</div>" +
                "<div class='cupom-section'>" +
                "<p class='section-title cupom-title'>Código do Cupom (Apresente na empresa):</p>" +
                "<div class='codigo codigo-cupom'>" + codigoCupom + "</div>" +
                "<p class='info-text'>Mostre este código no estabelecimento para resgatar sua vantagem</p>" +
                "</div>" +
                "<div class='conferencia-section'>" +
                "<p class='section-title conferencia-title'>Código de Conferência (Para sua referência):</p>" +
                "<div class='codigo codigo-conferencia'>" + codigoConferencia + "</div>" +
                "<p class='info-text'>Guarde este código para referência futura em caso de dúvidas</p>" +
                "</div>" +
                "<p class='info-text'>Aproveite sua recompensa! Este é o reconhecimento pelo seu esforço e dedicação.</p>"
                +
                "</div>" +
                "<div class='footer'>" +
                "<p>Sistema de Moedas Acadêmicas</p>" +
                "</div>" +
                "</div>" +
                "</body>" +
                "</html>";
    }

    private String gerarHtmlParceiro(Aluno aluno, Vantagem vantagem, String codigoCupom, String codigoConferencia) {
        return "<!DOCTYPE html>" +
                "<html lang='pt-BR'>" +
                "<head>" +
                "<meta charset='UTF-8'>" +
                "<meta name='viewport' content='width=device-width, initial-scale=1.0'>" +
                "<title>Novo Resgate Realizado</title>" +
                "<style>" +
                "body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; margin: 0; padding: 0; background-color: #f8f9fa; }"
                +
                ".container { max-width: 600px; margin: 0 auto; background-color: white; border-radius: 12px; box-shadow: 0 4px 6px rgba(0,0,0,0.1); overflow: hidden; }"
                +
                ".header { background: linear-gradient(135deg, #f59e0b, #d97706); color: white; padding: 30px 20px; text-align: center; }"
                +
                ".header h1 { margin: 0; font-size: 28px; font-weight: bold; }" +
                ".content { padding: 30px; }" +
                ".card { background-color: #fffbeb; border: 1px solid #fcd34d; border-radius: 8px; padding: 20px; margin-bottom: 20px; }"
                +
                ".aluno-info { background-color: #f0f9ff; border: 1px solid #bae6fd; border-radius: 8px; padding: 20px; margin-bottom: 20px; }"
                +
                ".vantagem-title { color: #d97706; font-size: 20px; font-weight: bold; margin-bottom: 10px; }" +
                ".vantagem-desc { color: #6b7280; line-height: 1.5; }" +
                ".cupom-section { background-color: #fef3c7; border: 1px solid #fcd34d; border-radius: 8px; padding: 20px; text-align: center; margin: 15px 0; }"
                +
                ".conferencia-section { background-color: #ecfdf5; border: 1px solid #a7f3d0; border-radius: 8px; padding: 20px; text-align: center; margin: 15px 0; }"
                +
                ".codigo { font-size: 24px; font-weight: bold; letter-spacing: 2px; margin: 10px 0; }" +
                ".codigo-cupom { color: #d97706; }" +
                ".codigo-conferencia { color: #047857; }" +
                ".info-text { color: #6b7280; font-size: 14px; line-height: 1.5; }" +
                ".footer { background-color: #f1f5f9; padding: 20px; text-align: center; color: #64748b; font-size: 12px; }"
                +
                ".icon { font-size: 24px; margin-bottom: 10px; }" +
                ".label { font-weight: 600; color: #374151; }" +
                ".section-title { margin: 0 0 10px 0; font-weight: 500; }" +
                ".cupom-title { color: #92400e; }" +
                ".conferencia-title { color: #065f46; }" +
                "</style>" +
                "</head>" +
                "<body>" +
                "<div class='container'>" +
                "<div class='header'>" +
                "<div class='icon'>📬</div>" +
                "<h1>Novo resgate realizado!</h1>" +
                "</div>" +
                "<div class='content'>" +
                "<div class='aluno-info'>" +
                "<p><span class='label'>Aluno:</span> " + aluno.getNome() + "</p>" +
                "<p><span class='label'>Email:</span> " + aluno.getEmail() + "</p>" +
                "</div>" +
                "<div class='card'>" +
                "<div class='vantagem-title'>" + vantagem.getNome() + "</div>" +
                "<div class='vantagem-desc'>" + vantagem.getDescricao() + "</div>" +
                "</div>" +
                "<div class='cupom-section'>" +
                "<p class='section-title cupom-title'>Código do Cupom (Para validação):</p>" +
                "<div class='codigo codigo-cupom'>" + codigoCupom + "</div>" +
                "<p class='info-text'>Valide este código quando o aluno apresentar para resgatar a vantagem</p>" +
                "</div>" +
                "<div class='conferencia-section'>" +
                "<p class='section-title conferencia-title'>Código de Conferência (Sistema):</p>" +
                "<div class='codigo codigo-conferencia'>" + codigoConferencia + "</div>" +
                "<p class='info-text'>Código interno do sistema para conferência e controle</p>" +
                "</div>" +
                "<p class='info-text'>Ambos os códigos devem corresponder para validação do resgate.</p>" +
                "</div>" +
                "<div class='footer'>" +
                "<p>Sistema de Moedas Acadêmicas - Empresa Parceira</p>" +
                "</div>" +
                "</div>" +
                "</body>" +
                "</html>";
    }
}