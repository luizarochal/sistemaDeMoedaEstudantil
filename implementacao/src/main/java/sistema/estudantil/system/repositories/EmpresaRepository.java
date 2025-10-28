package sistema.estudantil.system.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import sistema.estudantil.system.models.Empresa;

@Repository
public interface EmpresaRepository extends JpaRepository<Empresa, String> {
    // A chave primária (ID) de Empresa é String (cnpj)
}