package sistema.estudantil.system.models;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;

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

     @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(columnDefinition = "MEDIUMBLOB", name = "dados_imagem", nullable = false)
    private byte[] dadosImagem; // Armazena a imagem como um array de bytes

    @Column(name = "nome_arquivo", nullable = false, length = 255)
    private String nomeArquivo;

    @Column(name = "tipo_mime", nullable = false, length = 100)
    private String tipoMime; // "image/jpeg", "image/png" etc.

    @Positive
    @Column(name = "tamanho_bytes", nullable = false)
    private Long tamanhoBytes;

    @Column(name = "data_upload", nullable = false, updatable = false)
    private LocalDateTime dataUpload;

    @Column(name = "largura_pixels")
    private Integer largura;

    @Column(name = "altura_pixels")
    private Integer altura;
    
    private String cupom; 

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "empresa_cnpj", nullable = false) 
    @JsonIgnoreProperties({"vantagens", "password", "email"})
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

    public byte[] getDadosImagem() {
        return dadosImagem;
    }

    public void setDadosImagem(byte[] dadosImagem) {
        this.dadosImagem = dadosImagem;
    }

    public String getNomeArquivo() {
        return nomeArquivo;
    }

    public void setNomeArquivo(String nomeArquivo) {
        this.nomeArquivo = nomeArquivo;
    }

    public String getTipoMime() {
        return tipoMime;
    }

    public void setTipoMime(String tipoMime) {
        this.tipoMime = tipoMime;
    }

    public Long getTamanhoBytes() {
        return tamanhoBytes;
    }

    public void setTamanhoBytes(Long tamanhoBytes) {
        this.tamanhoBytes = tamanhoBytes;
    }

    public LocalDateTime getDataUpload() {
        return dataUpload;
    }

    public void setDataUpload(LocalDateTime dataUpload) {
        this.dataUpload = dataUpload;
    }

    public Integer getLargura() {
        return largura;
    }

    public void setLargura(Integer largura) {
        this.largura = largura;
    }
    
    public Integer getAltura() {
        return altura;
    }

    public void setAltura(Integer altura) {
        this.altura = altura;
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