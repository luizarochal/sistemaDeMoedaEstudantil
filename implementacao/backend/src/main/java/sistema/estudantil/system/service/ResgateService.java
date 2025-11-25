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
        emailService.enviarEmailCupomAluno(aluno.getEmail(), vantagem, codigoCupom);
        emailService.enviarEmailNotificacaoParceiro(empresa.getEmail(), aluno, vantagem, codigoCupom);

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
                    ? resgate.getVantagem().getEmpresaDono().getNome() : null,
                resgate.getCodigoCupom(),
                resgate.getDataResgate(),
                resgate.isUtilizado()
        );
    }

    private String gerarCodigoCupom() {
        return "CUPOM-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}