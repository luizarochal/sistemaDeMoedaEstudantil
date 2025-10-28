package sistema.estudantil.system.models;

import jakarta.persistence.*;

@Entity
@Table(name = "usuario_aluno_empresa")
@PrimaryKeyJoinColumn(name = "usuario_id")
public class UsuarioAlunoEmpresa extends Usuario {
    @Column(name = "endereco")
    private String endereco;

    public UsuarioAlunoEmpresa() {
        super();
    }

    public UsuarioAlunoEmpresa(String nome, String password, String endereco) {
        super(nome, password);
        this.endereco = endereco;

    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }
}