package sistema.estudantil.system.models;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;

@Entity
@Table(name = "professores")
@PrimaryKeyJoinColumn(name = "usuario_id")
public class Professor extends Usuario {

    @Column(nullable = false)
    private String departamento;

    @Column(nullable = false)
    private int quantidadeMoedas;

    @OneToMany(mappedBy = "professor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<TransacaoProf> listaDeTransacoes;

    public String getDepartamento() {
        return departamento;
    }

    public int getQuantidadeMoedas() {
        return quantidadeMoedas;
    }
    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }
    public void setQuantidadeMoedas(int quantidadeMoedas) {
        this.quantidadeMoedas = quantidadeMoedas;
    }
}
