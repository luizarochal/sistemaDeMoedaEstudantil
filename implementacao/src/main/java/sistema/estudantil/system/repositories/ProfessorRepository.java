package sistema.estudantil.system.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sistema.estudantil.system.models.Professor;

@Repository
public interface ProfessorRepository extends JpaRepository<Professor, Long> {
}