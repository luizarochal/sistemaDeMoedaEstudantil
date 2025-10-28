package sistema.estudantil.system.repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import sistema.estudantil.system.models.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

}