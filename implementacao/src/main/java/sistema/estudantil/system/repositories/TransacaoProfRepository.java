package sistema.estudantil.system.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import sistema.estudantil.system.models.TransacaoProf;
import org.springframework.stereotype.Repository;  

@Repository
public interface TransacaoProfRepository extends JpaRepository<TransacaoProf, Long> {
}