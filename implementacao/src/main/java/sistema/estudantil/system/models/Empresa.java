package sistema.estudantil.system.models;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "empresas")
@PrimaryKeyJoinColumn(name = "usuario_id")
public class Empresa extends Usuario {

    @Column(nullable = false, unique = true)
    private String cnpj;

    private String endereco;

    @OneToMany(mappedBy = "empresaDono", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Vantagem> vantagens = new ArrayList<>();

    public Empresa() {
        super();
    }

    public Empresa(String nome, String password, String email, String cnpj, String endereco) {
        super(nome, password, email);
        this.cnpj = cnpj;
        this.endereco = endereco;
    }

    public void addVantagem(Vantagem vantagem) {
        this.vantagens.add(vantagem);
        vantagem.setEmpresaDono(this);
    }

    public boolean removeVantagem(Long idVantagem) {
        return this.vantagens.removeIf(vantagem -> {
            if (vantagem.getIdVantagem().equals(idVantagem)) {
                vantagem.setEmpresaDono(null);
                return true;
            }
            return false;
        });
    }

    // Getters e Setters
    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public List<Vantagem> getVantagens() {
        return vantagens;
    }

    public void setVantagens(List<Vantagem> vantagens) {
        this.vantagens = vantagens;
    }
}