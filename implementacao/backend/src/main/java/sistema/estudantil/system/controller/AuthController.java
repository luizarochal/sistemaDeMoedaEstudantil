package sistema.estudantil.system.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import sistema.estudantil.system.dtos.*;
import sistema.estudantil.system.infra.security.TokenService;
import sistema.estudantil.system.models.*;
import sistema.estudantil.system.repositories.UsuarioRepository;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid LoginRequestDTO data) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.email(), data.password());
        var auth = authenticationManager.authenticate(usernamePassword);
        
        var usuario = (Usuario) auth.getPrincipal();
        var token = tokenService.generateToken(usuario);
        
        // CORREÇÃO: Passando os parâmetros na ordem correta
        return ResponseEntity.ok(new LoginResponseDTO(
            token, 
            usuario.getNome(), 
            usuario.getEmail(), 
            usuario.getRole()
        ));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid RegisterRequestDTO data) {
        if (usuarioRepository.existsByEmail(data.email())) {
            return ResponseEntity.badRequest().body("Email já cadastrado");
        }
        
        // PROFESSOR NÃO PODE SE CADASTRAR
        if ("ROLE_PROFESSOR".equals(data.role())) {
            return ResponseEntity.badRequest().body("Cadastro de professor não permitido");
        }
        
        String encryptedPassword = new BCryptPasswordEncoder().encode(data.password());
        Usuario novoUsuario;
        
        switch (data.role()) {
            case "ROLE_ALUNO":
                if (data.cpf() == null || data.rg() == null || data.instituicao() == null || data.curso() == null) {
                    return ResponseEntity.badRequest().body("Campos obrigatórios faltando para aluno: CPF, RG, instituição e curso");
                }
                novoUsuario = new Aluno(
                    data.nome(), 
                    data.cpf(), 
                    encryptedPassword, 
                    data.email(),
                    data.rg(), 
                    data.endereco(), 
                    data.instituicao(), 
                    data.curso()
                );
                break;
                
            case "ROLE_EMPRESA":
                if (data.cnpj() == null) {
                    return ResponseEntity.badRequest().body("CNPJ obrigatório para empresa");
                }
                novoUsuario = new Empresa(
                    data.nome(), 
                    encryptedPassword, 
                    data.email(),
                    data.cnpj(), 
                    data.endereco()
                );
                break;
                
            default:
                return ResponseEntity.badRequest().body("Tipo de usuário inválido. Use: ROLE_ALUNO ou ROLE_EMPRESA");
        }
        
        usuarioRepository.save(novoUsuario);
        return ResponseEntity.ok("Usuário cadastrado com sucesso");
    }
}