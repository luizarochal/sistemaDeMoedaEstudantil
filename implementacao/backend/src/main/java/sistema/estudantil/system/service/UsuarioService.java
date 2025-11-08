package sistema.estudantil.system.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import sistema.estudantil.system.models.Usuario;
import sistema.estudantil.system.repositories.UsuarioRepository;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public List<Usuario> findAll() {
        return usuarioRepository.findAll();
    }

    public Optional<Usuario> findById(Long id) {
        return usuarioRepository.findById(id);
    }

    public Usuario save(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    public void deleteById(Long id) {
        usuarioRepository.deleteById(id);
    }

    public Optional<Usuario> authenticate(String email, String password) {
        return usuarioRepository.findByEmailAndPassword(email, password);
    }
}
