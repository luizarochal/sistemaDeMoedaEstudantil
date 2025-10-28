package sistema.estudantil.system.models;

import jakarta.persistence.*;

@Entity
@Table(name = "alunos") 
@PrimaryKeyJoinColumn(name = "idUsuario")
public class Aluno extends UsuarioAlunoEmpresa {
    
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idAluno;

    private String email;
    private String rg;
    private String instituicao;
    private String curso;
    private float quantidadeMoedas;

    public Aluno(String email, String rg, String instituicao, String curso, float quantidadeMoedas) {
        super();
        this.email = email;
        this.rg = rg;
        this.instituicao = instituicao;
        this.curso = curso;
        this.quantidadeMoedas = quantidadeMoedas;
    }

    public Aluno() {
        super();
    }
    public Long getIdAluno() {
        return idAluno;
    }
    public void setIdAluno(Long idAluno) {
        this.idAluno = idAluno;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getRg() {
        return rg;
    }
    public void setRg(String rg) {
        this.rg = rg;
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
    
}

