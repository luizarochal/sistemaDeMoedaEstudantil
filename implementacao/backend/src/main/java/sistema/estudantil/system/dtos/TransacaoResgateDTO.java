package sistema.estudantil.system.dtos;

import java.time.LocalDateTime;

public class TransacaoResgateDTO {
    private Long alunoId;
    private String alunoNome;
    private Long vantagemId;
    private String vantagemNome;
    private Float custoMoedas;
    private String codigoCupom;
    private LocalDateTime dataResgate;

    // Construtores
    public TransacaoResgateDTO() {}

    public TransacaoResgateDTO(Long alunoId, String alunoNome, Long vantagemId, 
                              String vantagemNome, Float custoMoedas, 
                              String codigoCupom, LocalDateTime dataResgate) {
        this.alunoId = alunoId;
        this.alunoNome = alunoNome;
        this.vantagemId = vantagemId;
        this.vantagemNome = vantagemNome;
        this.custoMoedas = custoMoedas;
        this.codigoCupom = codigoCupom;
        this.dataResgate = dataResgate;
    }

    // Getters e Setters
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