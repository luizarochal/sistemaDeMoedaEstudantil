package sistema.estudantil.system.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import jakarta.transaction.Transactional;
import sistema.estudantil.system.models.Professor;
import sistema.estudantil.system.repositories.ProfessorRepository;

public class ProfessorService {
    @Autowired
    private ProfessorRepository professorRepository;

    @Transactional
    public Professor salvarProfessor(Professor professor) {
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
}
