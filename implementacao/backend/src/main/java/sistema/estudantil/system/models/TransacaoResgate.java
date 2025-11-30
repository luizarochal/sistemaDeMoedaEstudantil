package sistema.estudantil.system.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "transacoes_resgate")
public class TransacaoResgate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "aluno_id", nullable = false)
    private Long alunoId;

    @Column(name = "aluno_nome", nullable = false)
    private String alunoNome;

    @Column(name = "vantagem_id", nullable = false)
    private Long vantagemId;

    @Column(name = "vantagem_nome", nullable = false)
    private String vantagemNome;

    @Column(name = "custo_moedas", nullable = false)
    private Float custoMoedas;

    @Column(name = "codigo_cupom", nullable = false)
    private String codigoCupom;

    @Column(name = "data_resgate", nullable = false)
    private LocalDateTime dataResgate;

    // Construtores
    public TransacaoResgate() {
        this.dataResgate = LocalDateTime.now();
    }

    public TransacaoResgate(Long alunoId, String alunoNome, Long vantagemId, 
                           String vantagemNome, Float custoMoedas, String codigoCupom) {
        this();
        this.alunoId = alunoId;
        this.alunoNome = alunoNome;
        this.vantagemId = vantagemId;
        this.vantagemNome = vantagemNome;
        this.custoMoedas = custoMoedas;
        this.codigoCupom = codigoCupom;
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getAlunoId() { return alunoId; }
    public void setAlunoId(Long alunoId) { this.alunoId = alunoId; }

    public String getAlunoNome() { return alunoNome; }
    public void setAlunoNome(String alunoNome) { this.alunoNome = alunoNome; }

    public Long getVantagemId() { return vantagemId; }
    public void setVantagemId(Long vantagemId) { this.vantagemId = vantagemId; }

    public String getVantagemNome() { return vantagemNome; }
    public void setVantagemNome(String vantagemNome) { this.vantagemNome = vantagemNome; }

    public Float getCustoMoedas() { return custoMoedas; }
    public void setCustoMoedas(Float custoMoedas) { this.custoMoedas = custoMoedas; }

    public String getCodigoCupom() { return codigoCupom; }
    public void setCodigoCupom(String codigoCupom) { this.codigoCupom = codigoCupom; }

    public LocalDateTime getDataResgate() { return dataResgate; }
    public void setDataResgate(LocalDateTime dataResgate) { this.dataResgate = dataResgate; }
}