package sistema.estudantil.system.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;

@Entity
@Table(name = "vantagens")
public class Vantagem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idVantagem;

    @Column(nullable = false)
    private String nome;

    private String descricao;
    
    @Column(nullable = false)
    private float custo;

    private String foto; 
    
    private String cupom; 

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "empresa_cnpj", nullable = false) 
    private Empresa empresaDono;


    public Long getIdVantagem() {
        return idVantagem;
    }

    public void setIdVantagem(Long idVantagem) {
        this.idVantagem = idVantagem;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public float getCusto() {
        return custo;
    }

    public void setCusto(float custo) {
        this.custo = custo;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getCupom() {
        return cupom;
    }

    public void setCupom(String cupom) {
        this.cupom = cupom;
    }

    public Empresa getEmpresaDono() {
        return empresaDono;
    }

    public void setEmpresaDono(Empresa empresaDono) {
        this.empresaDono = empresaDono;
    }
}