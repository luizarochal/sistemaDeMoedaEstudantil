package sistema.estudantil.system.controller;

import java.util.List;
import java.util.Optional;

import sistema.estudantil.system.dtos.CriarUsuarioDTO;
import sistema.estudantil.system.models.Usuario;
import sistema.estudantil.system.service.UsuarioService;

public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    public List<Usuario> findAll() {
        return usuarioService.findAll();
    }

    public Optional<Usuario> findById(Long id) {
        return usuarioService.findById(id);
    }

    public Usuario create(CriarUsuarioDTO criarUsuarioDTO) {
        Usuario usuario = new Usuario();
        usuario.setName(criarUsuarioDTO.getName());
        usuario.setCPF(criarUsuarioDTO.getCPF());
        usuario.setPassword(criarUsuarioDTO.getPassword());
        return usuarioService.save(usuario);
    }

    public void deleteById(Long id) {
        usuarioService.deleteById(id);
    }
}
