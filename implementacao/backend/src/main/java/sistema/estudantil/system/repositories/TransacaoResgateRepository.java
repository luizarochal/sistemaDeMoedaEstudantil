package sistema.estudantil.system.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sistema.estudantil.system.models.TransacaoResgate;

import java.util.List;

@Repository
public interface TransacaoResgateRepository extends JpaRepository<TransacaoResgate, Long> {
    List<TransacaoResgate> findByAlunoId(Long alunoId);
}