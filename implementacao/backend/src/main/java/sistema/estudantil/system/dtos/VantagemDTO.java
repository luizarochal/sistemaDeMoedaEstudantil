package sistema.estudantil.system.dtos;

public class VantagemDTO {
    private Long idVantagem;
    private String nome;
    private String descricao;
    private float custo;
    private String cupom;
    private String empresaNome;
    private String empresaCnpj;
    
    // Novos campos para a imagem
    private String nomeArquivo;
    private String tipoMime;
    private Long tamanhoBytes;
    private Integer largura;
    private Integer altura;

    // Construtores
    public VantagemDTO() {}

    public VantagemDTO(Long idVantagem, String nome, String descricao, float custo, 
                      String cupom, String empresaNome, String empresaCnpj,
                      String nomeArquivo, String tipoMime, Long tamanhoBytes, 
                      Integer largura, Integer altura) {
        this.idVantagem = idVantagem;
        this.nome = nome;
        this.descricao = descricao;
        this.custo = custo;
        this.cupom = cupom;
        this.empresaNome = empresaNome;
        this.empresaCnpj = empresaCnpj;
        this.nomeArquivo = nomeArquivo;
        this.tipoMime = tipoMime;
        this.tamanhoBytes = tamanhoBytes;
        this.largura = largura;
        this.altura = altura;
    }

    // Getters e Setters
    public Long getIdVantagem() { return idVantagem; }
    public void setIdVantagem(Long idVantagem) { this.idVantagem = idVantagem; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public float getCusto() { return custo; }
    public void setCusto(float custo) { this.custo = custo; }

    public String getCupom() { return cupom; }
    public void setCupom(String cupom) { this.cupom = cupom; }

    public String getEmpresaNome() { return empresaNome; }
    public void setEmpresaNome(String empresaNome) { this.empresaNome = empresaNome; }

    public String getEmpresaCnpj() { return empresaCnpj; }
    public void setEmpresaCnpj(String empresaCnpj) { this.empresaCnpj = empresaCnpj; }

    public String getNomeArquivo() { return nomeArquivo; }
    public void setNomeArquivo(String nomeArquivo) { this.nomeArquivo = nomeArquivo; }

    public String getTipoMime() { return tipoMime; }
    public void setTipoMime(String tipoMime) { this.tipoMime = tipoMime; }

    public Long getTamanhoBytes() { return tamanhoBytes; }
    public void setTamanhoBytes(Long tamanhoBytes) { this.tamanhoBytes = tamanhoBytes; }

    public Integer getLargura() { return largura; }
    public void setLargura(Integer largura) { this.largura = largura; }

    public Integer getAltura() { return altura; }
    public void setAltura(Integer altura) { this.altura = altura; }
}