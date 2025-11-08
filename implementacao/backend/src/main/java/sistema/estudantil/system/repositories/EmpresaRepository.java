package sistema.estudantil.system.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import sistema.estudantil.system.models.Empresa;

@Repository
public interface EmpresaRepository extends JpaRepository<Empresa, Long> {
    boolean existsByCnpj(String cnpj);
    Optional<Empresa> findByCnpj(String cnpj);
    void deleteByCnpj(String cnpj);

}