package sistema.estudantil.system.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sistema.estudantil.system.models.TransacaoResgate;
import sistema.estudantil.system.repositories.TransacaoResgateRepository;
import sistema.estudantil.system.dtos.TransacaoResgateDTO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransacaoResgateService {

    @Autowired
    private TransacaoResgateRepository transacaoResgateRepository;

    @Transactional
    public void registrarTransacaoResgate(Long alunoId, String alunoNome, Long vantagemId, 
                                        String vantagemNome, Float custoMoedas, String codigoCupom) {
        TransacaoResgate transacao = new TransacaoResgate();
        transacao.setAlunoId(alunoId);
        transacao.setAlunoNome(alunoNome);
        transacao.setVantagemId(vantagemId);
        transacao.setVantagemNome(vantagemNome);
        transacao.setCustoMoedas(custoMoedas);
        transacao.setCodigoCupom(codigoCupom);
        transacao.setDataResgate(LocalDateTime.now());

        transacaoResgateRepository.save(transacao);
    }

    private TransacaoResgateDTO toDTO(TransacaoResgate transacao) {
        return new TransacaoResgateDTO(
            transacao.getAlunoId(),
            transacao.getAlunoNome(),
            transacao.getVantagemId(),
            transacao.getVantagemNome(),
            transacao.getCustoMoedas(),
            transacao.getCodigoCupom(),
            transacao.getDataResgate()
        );
    }

    @Transactional(readOnly = true)
    public List<TransacaoResgateDTO> listarTransacoesResgatePorAluno(Long alunoId) {
        List<TransacaoResgate> transacoes = transacaoResgateRepository.findByAlunoId(alunoId);
        return transacoes.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<TransacaoResgateDTO> listarTodasTransacoesResgate() {
        List<TransacaoResgate> transacoes = transacaoResgateRepository.findAll();
        return transacoes.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
}