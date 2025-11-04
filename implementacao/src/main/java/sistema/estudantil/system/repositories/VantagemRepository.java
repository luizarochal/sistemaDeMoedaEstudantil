package sistema.estudantil.system.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

import sistema.estudantil.system.models.Vantagem;

@Repository
public interface VantagemRepository extends JpaRepository<Vantagem, Long> {
    // A chave primária (ID) de Vantagem é Long (idVantagem)

    // Método customizado para buscar todas as vantagens de uma empresa específica
    List<Vantagem> findByEmpresaDonoCnpj(String cnpj);
}