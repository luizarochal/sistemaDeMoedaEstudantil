package sistema.estudantil.system.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record RegisterRequestDTO(
    @NotBlank String nome,
    @NotBlank @Email String email,
    @NotBlank String password,
    @NotBlank String role, // "ROLE_ALUNO", "ROLE_EMPRESA" - PROFESSOR NÃO SE CADASTRA!
    
    // Campos específicos
    String cpf,    // Para Aluno
    String cnpj,   // Para Empresa  
    String rg,     // Para Aluno
    String endereco,
    String instituicao,
    String curso   // Para Aluno
) {}