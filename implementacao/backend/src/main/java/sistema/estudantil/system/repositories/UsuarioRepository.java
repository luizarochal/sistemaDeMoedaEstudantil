package sistema.estudantil.system.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import sistema.estudantil.system.models.Usuario;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String email);
    boolean existsByEmail(String email);
    
    // REMOVA este método se existir:
    // Optional<Usuario> findByEmailAndPassword(String email, String password); // ❌
}