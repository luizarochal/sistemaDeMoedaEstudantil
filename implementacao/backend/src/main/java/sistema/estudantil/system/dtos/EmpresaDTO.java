// EmpresaDTO.java
package sistema.estudantil.system.dtos;

public class EmpresaDTO {
    private String nome;
    private String email;
    private String cnpj;
    private String endereco;
    
    // Construtores, getters e setters
    public EmpresaDTO() {}
    
    public EmpresaDTO(String nome, String email, String cnpj, String endereco) {
        this.nome = nome;
        this.email = email;
        this.cnpj = cnpj;
        this.endereco = endereco;
    }
    
    // Getters e Setters
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getCnpj() { return cnpj; }
    public void setCnpj(String cnpj) { this.cnpj = cnpj; }
    
    public String getEndereco() { return endereco; }
    public void setEndereco(String endereco) { this.endereco = endereco; }
}