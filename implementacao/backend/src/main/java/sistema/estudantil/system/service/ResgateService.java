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

import java.util.List;
import java.util.UUID;

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

    @Transactional
    public Resgate resgatarVantagem(@NonNull Long alunoId, @NonNull Long vantagemId) {
        Aluno aluno = alunoService.findById(alunoId)
                .orElseThrow(() -> new RuntimeException("Aluno não encontrado com ID: " + alunoId));

        // USAR O NOVO MÉTODO que retorna a entidade Vantagem completa
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

        // Enviar emails
        Empresa empresa = vantagem.getEmpresaDono();
        emailService.enviarEmailCupomAluno(aluno.getEmail(), vantagem, codigoCupom);
        emailService.enviarEmailNotificacaoParceiro(empresa.getEmail(), aluno, vantagem, codigoCupom);

        return resgateSalvo;
    }

    public List<Resgate> listarResgatesPorAluno(Long alunoId) {
        return resgateRepository.findByAlunoId(alunoId);
    }

    public List<Resgate> listarResgatesPorEmpresa(String cnpj) {
        return resgateRepository.findByVantagemEmpresaDonoCnpj(cnpj);
    }

    @Transactional
    public void marcarCupomComoUtilizado(@NonNull Long resgateId) {
        Resgate resgate = resgateRepository.findById(resgateId)
                .orElseThrow(() -> new RuntimeException("Resgate não encontrado com ID: " + resgateId));

        resgate.setUtilizado(true);
        resgateRepository.save(resgate);
    }

    public Resgate obterResgatePorId(@NonNull Long id) {
        return resgateRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Resgate não encontrado com ID: " + id));
    }

    private String gerarCodigoCupom() {
        return "CUPOM-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}