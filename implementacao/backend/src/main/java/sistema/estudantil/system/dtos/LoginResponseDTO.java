package sistema.estudantil.system.dtos;

public record LoginResponseDTO(
        String token,
        String nome,
        String email,
        String role) {
}