package sistema.estudantil.system.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

import sistema.estudantil.system.models.Vantagem;

@Repository
public interface VantagemRepository extends JpaRepository<Vantagem, Long> {
    List<Vantagem> findByEmpresaDonoCnpj(String cnpj);

     // Método customizado para carregar imagem
    @Query("SELECT v FROM Vantagem v LEFT JOIN FETCH v.empresaDono WHERE v.idVantagem = :id")
    Optional<Vantagem> findByIdWithImage(@Param("id") Long id);

    //metodo para buscar vanategns que ainda não houveram resgates
    @Query("SELECT v FROM Vantagem v WHERE v.idVantagem NOT IN (SELECT r.vantagem.idVantagem FROM Resgate r)")
    List<Vantagem> findVantagensSemResgates();
}