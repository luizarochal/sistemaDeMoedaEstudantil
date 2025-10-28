package sistema.estudantil.system.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import sistema.estudantil.system.models.UsuarioAlunoEmpresa;
import org.springframework.stereotype.Repository;


@Repository
public interface UsuarioAlunoEmpresaRepository extends JpaRepository<UsuarioAlunoEmpresa, Long> {

}