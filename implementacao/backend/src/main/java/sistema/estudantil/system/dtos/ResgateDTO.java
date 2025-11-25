package sistema.estudantil.system.dtos;

import java.time.LocalDateTime;

public class ResgateDTO {
    private Long id;
    private Long alunoId;
    private String alunoNome;
    private Long vantagemId;
    private String vantagemNome;
    private String vantagemDescricao;
    private Float vantagemCusto;
    private String empresaNome;
    private String codigoCupom;
    private LocalDateTime dataResgate;
    private Boolean utilizado;

    // Construtores
    public ResgateDTO() {
    }

    public ResgateDTO(Long id, Long alunoId, String alunoNome, Long vantagemId,
            String vantagemNome, String vantagemDescricao, Float vantagemCusto,
            String empresaNome, String codigoCupom, LocalDateTime dataResgate,
            Boolean utilizado) {
        this.id = id;
        this.alunoId = alunoId;
        this.alunoNome = alunoNome;
        this.vantagemId = vantagemId;
        this.vantagemNome = vantagemNome;
        this.vantagemDescricao = vantagemDescricao;
        this.vantagemCusto = vantagemCusto;
        this.empresaNome = empresaNome;
        this.codigoCupom = codigoCupom;
        this.dataResgate = dataResgate;
        this.utilizado = utilizado;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAlunoId() {
        return alunoId;
    }

    public void setAlunoId(Long alunoId) {
        this.alunoId = alunoId;
    }

    public String getAlunoNome() {
        return alunoNome;
    }

    public void setAlunoNome(String alunoNome) {
        this.alunoNome = alunoNome;
    }

    public Long getVantagemId() {
        return vantagemId;
    }

    public void setVantagemId(Long vantagemId) {
        this.vantagemId = vantagemId;
    }

    public String getVantagemNome() {
        return vantagemNome;
    }

    public void setVantagemNome(String vantagemNome) {
        this.vantagemNome = vantagemNome;
    }

    public String getVantagemDescricao() {
        return vantagemDescricao;
    }

    public void setVantagemDescricao(String vantagemDescricao) {
        this.vantagemDescricao = vantagemDescricao;
    }

    public Float getVantagemCusto() {
        return vantagemCusto;
    }

    public void setVantagemCusto(Float vantagemCusto) {
        this.vantagemCusto = vantagemCusto;
    }

    public String getEmpresaNome() {
        return empresaNome;
    }

    public void setEmpresaNome(String empresaNome) {
        this.empresaNome = empresaNome;
    }

    public String getCodigoCupom() {
        return codigoCupom;
    }

    public void setCodigoCupom(String codigoCupom) {
        this.codigoCupom = codigoCupom;
    }

    public LocalDateTime getDataResgate() {
        return dataResgate;
    }

    public void setDataResgate(LocalDateTime dataResgate) {
        this.dataResgate = dataResgate;
    }

    public Boolean getUtilizado() {
        return utilizado;
    }

    public void setUtilizado(Boolean utilizado) {
        this.utilizado = utilizado;
    }
}