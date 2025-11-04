package sistema.estudantil.system.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sistema.estudantil.system.models.TransacaoProf;

import java.util.List;

@Repository
public interface TransacaoProfRepository extends JpaRepository<TransacaoProf, Long> {
    List<TransacaoProf> findByProfessorId(Long professorId);
    List<TransacaoProf> findByAlunoId(Long alunoId);
}