package sistema.estudantil.system.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sistema.estudantil.system.models.Professor;
import sistema.estudantil.system.repositories.ProfessorRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ProfessorService {
    
    @Autowired
    private ProfessorRepository professorRepository;

    @Transactional
    public Professor salvarProfessor(Professor professor) {
        if (professor.getDataUltimaRecarga() == null) {
            professor.setDataUltimaRecarga(LocalDateTime.now());
        }
        return professorRepository.save(professor);
    }

    public Optional<Professor> buscarProfessorPorId(Long id) {
        return professorRepository.findById(id);
    }

    public List<Professor> listarTodosProfessores() {
        return professorRepository.findAll();
    }

    @Transactional
    public void deletarProfessor(Long id) {
        professorRepository.deleteById(id);
    }

    @Scheduled(cron = "0 0 0 1 1,6 *") // Executa em 1º de janeiro e 1º de junho
    @Transactional
    public void recarregarMoedasSemestrais() {
        List<Professor> professores = professorRepository.findAll();
        LocalDateTime now = LocalDateTime.now();
        
        for (Professor professor : professores) {
            // Verifica se já passou pelo menos 6 meses da última recarga
            if (professor.getDataUltimaRecarga() == null || 
                professor.getDataUltimaRecarga().plusMonths(6).isBefore(now)) {
                
                professor.setQuantidadeMoedas(professor.getQuantidadeMoedas() + 1000);
                professor.setDataUltimaRecarga(now);
            }
        }
        professorRepository.saveAll(professores);
    }

    @Transactional
    public Professor atualizarMoedas(Long professorId, int quantidade) {
        Professor professor = professorRepository.findById(professorId)
                .orElseThrow(() -> new RuntimeException("Professor não encontrado com ID: " + professorId));
        
        professor.setQuantidadeMoedas(professor.getQuantidadeMoedas() + quantidade);
        return professorRepository.save(professor);
    }
}