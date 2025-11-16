package sistema.estudantil.system.dtos;

import java.time.LocalDateTime;

public class TransacaoProfDTO {
    private Long id;
    private Long professorId;
    private String professorNome;
    private Long alunoId;
    private String alunoNome;
    private int quantidadeMoedas;
    private String mensagem;
    private LocalDateTime dataTransacao;

    // Construtores
    public TransacaoProfDTO() {
    }

    public TransacaoProfDTO(Long id, Long professorId, String professorNome, Long alunoId, String alunoNome,
            int quantidadeMoedas, String mensagem, LocalDateTime dataTransacao) {
        this.id = id;
        this.professorId = professorId;
        this.professorNome = professorNome;
        this.alunoId = alunoId;
        this.alunoNome = alunoNome;
        this.quantidadeMoedas = quantidadeMoedas;
        this.mensagem = mensagem;
        this.dataTransacao = dataTransacao;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProfessorId() {
        return professorId;
    }

    public void setProfessorId(Long professorId) {
        this.professorId = professorId;
    }

    public String getProfessorNome() {
        return professorNome;
    }

    public void setProfessorNome(String professorNome) {
        this.professorNome = professorNome;
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

    public int getQuantidadeMoedas() {
        return quantidadeMoedas;
    }

    public void setQuantidadeMoedas(int quantidadeMoedas) {
        this.quantidadeMoedas = quantidadeMoedas;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public LocalDateTime getDataTransacao() {
        return dataTransacao;
    }

    public void setDataTransacao(LocalDateTime dataTransacao) {
        this.dataTransacao = dataTransacao;
    }
}