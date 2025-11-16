package sistema.estudantil.system.dtos;

public class VantagemDTO {
    private Long idVantagem;
    private String nome;
    private String descricao;
    private float custo;
    private String foto;
    private String cupom;
    private String empresaNome;
    private String empresaCnpj;

    // Construtores
    public VantagemDTO() {}

    public VantagemDTO(Long idVantagem, String nome, String descricao, float custo, 
                      String foto, String cupom, String empresaNome, String empresaCnpj) {
        this.idVantagem = idVantagem;
        this.nome = nome;
        this.descricao = descricao;
        this.custo = custo;
        this.foto = foto;
        this.cupom = cupom;
        this.empresaNome = empresaNome;
        this.empresaCnpj = empresaCnpj;
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

    public String getFoto() { return foto; }
    public void setFoto(String foto) { this.foto = foto; }

    public String getCupom() { return cupom; }
    public void setCupom(String cupom) { this.cupom = cupom; }

    public String getEmpresaNome() { return empresaNome; }
    public void setEmpresaNome(String empresaNome) { this.empresaNome = empresaNome; }

    public String getEmpresaCnpj() { return empresaCnpj; }
    public void setEmpresaCnpj(String empresaCnpj) { this.empresaCnpj = empresaCnpj; }
}