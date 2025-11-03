package sistema.estudantil.system.models;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "alunos")
@PrimaryKeyJoinColumn(name = "usuario_id")
public class Aluno extends Usuario {
    
    @Column(nullable = false)
    private String rg;
    
    private String endereco;
    
    @Column(nullable = false)
    private String instituicao;
    
    @Column(nullable = false)
    private String curso;
    
    private float quantidadeMoedas = 0;
    
    @OneToMany(mappedBy = "aluno", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<TransacaoProf> transacoesRecebidas = new ArrayList<>();
    
    @OneToMany(mappedBy = "aluno", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Resgate> resgates = new ArrayList<>();

    public Aluno() {
        super();
    }

    public Aluno(String nome, String cpf, String password, String email, String rg, String endereco, String instituicao, String curso) {
        super(nome, cpf, password, email);
        this.rg = rg;
        this.endereco = endereco;
        this.instituicao = instituicao;
        this.curso = curso;
    }

    // Getters e Setters
    public String getRg() {
        return rg;
    }

    public void setRg(String rg) {
        this.rg = rg;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getInstituicao() {
        return instituicao;
    }

    public void setInstituicao(String instituicao) {
        this.instituicao = instituicao;
    }

    public String getCurso() {
        return curso;
    }

    public void setCurso(String curso) {
        this.curso = curso;
    }

    public float getQuantidadeMoedas() {
        return quantidadeMoedas;
    }

    public void setQuantidadeMoedas(float quantidadeMoedas) {
        this.quantidadeMoedas = quantidadeMoedas;
    }

    public List<TransacaoProf> getTransacoesRecebidas() {
        return transacoesRecebidas;
    }

    public void setTransacoesRecebidas(List<TransacaoProf> transacoesRecebidas) {
        this.transacoesRecebidas = transacoesRecebidas;
    }

    public List<Resgate> getResgates() {
        return resgates;
    }

    public void setResgates(List<Resgate> resgates) {
        this.resgates = resgates;
    }
}