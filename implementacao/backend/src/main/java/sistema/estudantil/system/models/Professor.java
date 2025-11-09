package sistema.estudantil.system.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "professores")
@PrimaryKeyJoinColumn(name = "usuario_id")
public class Professor extends Usuario {

    @Column(nullable = false, unique = true)
    private String cpf;

    @Column(nullable = false)
    private String departamento;

    @Column(nullable = false)
    private String instituicao;

    @Column(nullable = false)
    private int quantidadeMoedas = 0;

    private LocalDateTime dataUltimaRecarga;

    @OneToMany(mappedBy = "professor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<TransacaoProf> transacoesRealizadas = new ArrayList<>();

    public Professor() {
        super();
        this.setRole("ROLE_PROFESSOR"); // 🔑 Define o tipo de usuário automaticamente
    }

    public Professor(String nome, String cpf, String password, String email, String departamento, String instituicao) {
         super(nome, password, email, "ROLE_PROFESSOR");
        this.cpf = cpf;
        this.departamento = departamento;
        this.instituicao = instituicao;
        this.dataUltimaRecarga = LocalDateTime.now();
    }

    // Getters e Setters
    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    public String getInstituicao() {
        return instituicao;
    }

    public void setInstituicao(String instituicao) {
        this.instituicao = instituicao;
    }

    public int getQuantidadeMoedas() {
        return quantidadeMoedas;
    }

    public void setQuantidadeMoedas(int quantidadeMoedas) {
        this.quantidadeMoedas = quantidadeMoedas;
    }

    public LocalDateTime getDataUltimaRecarga() {
        return dataUltimaRecarga;
    }

    public void setDataUltimaRecarga(LocalDateTime dataUltimaRecarga) {
        this.dataUltimaRecarga = dataUltimaRecarga;
    }

    public List<TransacaoProf> getTransacoesRealizadas() {
        return transacoesRealizadas;
    }

    public void setTransacoesRealizadas(List<TransacaoProf> transacoesRealizadas) {
        this.transacoesRealizadas = transacoesRealizadas;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }
}