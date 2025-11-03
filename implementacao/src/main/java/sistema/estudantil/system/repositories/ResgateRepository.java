package sistema.estudantil.system.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sistema.estudantil.system.models.Resgate;

import java.util.List;

@Repository
public interface ResgateRepository extends JpaRepository<Resgate, Long> {
    List<Resgate> findByAlunoId(Long alunoId);
    List<Resgate> findByVantagemEmpresaDonoCnpj(String cnpj);
    List<Resgate> findByVantagemId(Long vantagemId);
}