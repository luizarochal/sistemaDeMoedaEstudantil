package sistema.estudantil.system.models;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;

import sistema.estudantil.system.models.UsuarioAlunoEmpresa;
import sistema.estudantil.system.models.Vantagem;


@Entity
@Table(name = "empresa")
@PrimaryKeyJoinColumn(name = "empresa_id")
public class Empresa extends UsuarioAlunoEmpresa {

    @Id
    @Column(nullable = false, unique = true, length = 14)
    private String cnpj;

    @OneToMany(
        mappedBy = "empresaDono",
        cascade = CascadeType.ALL,
        orphanRemoval = true,
        fetch = FetchType.LAZY
    )
    private List<Vantagem> vantagens = new ArrayList<>();

    public Empresa() {
    }

    public Empresa(String cnpj) {
        this.cnpj = cnpj;
    }

    public void addVantagem(Vantagem vantagem) {
        this.vantagens.add(vantagem);
        vantagem.setEmpresaDono(this);
    }

    public String removeVantagem(Long idVantagem) {
        boolean removed = this.vantagens.removeIf(vantagem -> {
            if (vantagem.getIdVantagem().equals(idVantagem)) {
                vantagem.setEmpresaDono(null); 
            }
            return false;
        });
        
        return removed ? "Vantagem removida com sucesso." : "Vantagem não encontrada.";
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public List<Vantagem> getVantagens() {
        return vantagens;
    }

    public void setVantagens(List<Vantagem> vantagens) {
        this.vantagens = vantagens;
    }

}