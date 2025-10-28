package sistema.estudantil.system.repositories;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import sistema.estudantil.system.models.Aluno;
import org.springframework.stereotype.Repository;

@Repository
public interface AlunoRepository extends JpaRepository<Aluno, Long> {
    Optional<Aluno> findByEmail(String email);
    boolean existsByEmail(String email);
}

