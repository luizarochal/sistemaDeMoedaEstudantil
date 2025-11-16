package sistema.estudantil.system.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sistema.estudantil.system.config.ProfessorTxtConfig;
import sistema.estudantil.system.models.Professor;
import sistema.estudantil.system.repositories.ProfessorRepository;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProfessorTxtService {

    private static final Logger logger = LoggerFactory.getLogger(ProfessorTxtService.class);

    @Autowired
    private ProfessorTxtConfig txtConfig;

    @Autowired
    private ProfessorRepository professorRepository;

    @Autowired
    private PasswordEncoder passwordEncoder; // 🔑 Injeta o PasswordEncoder

    @EventListener(ApplicationReadyEvent.class)
    public void processarArquivoProfessoresAoIniciar() {
        if (!txtConfig.isEnabled()) {
            logger.info("Processamento de arquivo TXT de professores está desabilitado");
            return;
        }

        try {
            logger.info("Iniciando processamento do arquivo de professores: {}", txtConfig.getPath());
            File arquivo = new File(txtConfig.getPath());
            
            if (!arquivo.exists()) {
                // Tenta ler do classpath
                logger.info("Arquivo não encontrado no filesystem, tentando classpath...");
                List<Professor> professores = lerProfessoresDoClasspath();
                if (!professores.isEmpty()) {
                    processarProfessores(professores);
                    logger.info("Processamento do classpath concluído. Total de professores processados: {}", professores.size());
                } else {
                    logger.warn("Arquivo de professores não encontrado: {}", txtConfig.getPath());
                    criarArquivoExemplo();
                }
                return;
            }

            List<Professor> professores = lerProfessoresDoArquivo(arquivo);
            processarProfessores(professores);
            logger.info("Processamento concluído. Total de professores processados: {}", professores.size());
            
        } catch (Exception e) {
            logger.error("Erro ao processar arquivo de professores", e);
        }
    }

    private List<Professor> lerProfessoresDoArquivo(File arquivo) {
        List<Professor> professores = new ArrayList<>();
        
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(arquivo), StandardCharsets.UTF_8))) {
            
            String linha;
            int numeroLinha = 0;
            
            while ((linha = br.readLine()) != null) {
                numeroLinha++;
                linha = linha.trim();
                
                // Pular linhas vazias ou comentários
                if (linha.isEmpty() || linha.startsWith("#")) {
                    continue;
                }
                
                try {
                    Professor professor = parseLinhaProfessor(linha, numeroLinha);
                    if (professor != null) {
                        professores.add(professor);
                    }
                } catch (Exception e) {
                    logger.warn("Erro ao processar linha {}: {}", numeroLinha, linha, e);
                }
            }
            
        } catch (Exception e) {
            logger.error("Erro ao ler arquivo de professores", e);
        }
        
        return professores;
    }

    private List<Professor> lerProfessoresDoClasspath() {
        List<Professor> professores = new ArrayList<>();
        
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("professores.txt")) {
            if (inputStream == null) {
                return professores;
            }
            
            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
                
                String linha;
                int numeroLinha = 0;
                
                while ((linha = br.readLine()) != null) {
                    numeroLinha++;
                    linha = linha.trim();
                    
                    if (linha.isEmpty() || linha.startsWith("#")) {
                        continue;
                    }
                    
                    try {
                        Professor professor = parseLinhaProfessor(linha, numeroLinha);
                        if (professor != null) {
                            professores.add(professor);
                        }
                    } catch (Exception e) {
                        logger.warn("Erro ao processar linha {}: {}", numeroLinha, linha, e);
                    }
                }
            }
            
        } catch (Exception e) {
            logger.error("Erro ao ler arquivo do classpath", e);
        }
        
        return professores;
    }

    private Professor parseLinhaProfessor(String linha, int numeroLinha) {
        // Formato esperado: nome;cpf;email;password;departamento;instituicao
        String[] partes = linha.split(";");
        
        if (partes.length < 6) {
            logger.warn("Linha {} ignorada - formato inválido. Esperado: nome;cpf;email;password;departamento;instituicao", numeroLinha);
            return null;
        }
        
        String nome = partes[0].trim();
        String cpf = partes[1].trim();
        String email = partes[2].trim();
        String password = partes[3].trim();
        String departamento = partes[4].trim();
        String instituicao = partes[5].trim();
        
        // Validar campos obrigatórios
        if (nome.isEmpty() || cpf.isEmpty() || email.isEmpty() || password.isEmpty()) {
            logger.warn("Linha {} ignorada - campos obrigatórios faltando", numeroLinha);
            return null;
        }
        
        // 🔑 Criptografar a senha igual aos outros usuários
        String senhaCriptografada = passwordEncoder.encode(password);
        
        Professor professor = new Professor();
        professor.setNome(nome);
        professor.setCpf(cpf);
        professor.setEmail(email);
        professor.setPassword(senhaCriptografada); // 🔑 Senha criptografada
        professor.setDepartamento(departamento);
        professor.setInstituicao(instituicao);
        professor.setQuantidadeMoedas(1000); // Valor inicial
        professor.setDataUltimaRecarga(LocalDateTime.now());
        
        return professor;
    }

    @SuppressWarnings("null")
    @Transactional
    public void processarProfessores(List<Professor> professoresDoArquivo) {
        int criados = 0;
        int atualizados = 0;
        
        for (Professor professorArquivo : professoresDoArquivo) {
            try {
                // Buscar professor por CPF (campo único)
                Optional<Professor> professorExistente = professorRepository.findAll().stream()
                        .filter(p -> p.getCpf().equals(professorArquivo.getCpf()))
                        .findFirst();
                
                if (professorExistente.isPresent()) {
                    // Atualizar professor existente
                    Professor professor = professorExistente.get();
                    professor.setNome(professorArquivo.getNome());
                    professor.setEmail(professorArquivo.getEmail());
                    
                    // 🔑 Atualizar senha apenas se for diferente
                    if (!professorArquivo.getPassword().equals(professor.getPassword())) {
                        professor.setPassword(professorArquivo.getPassword());
                    }
                    
                    professor.setDepartamento(professorArquivo.getDepartamento());
                    professor.setInstituicao(professorArquivo.getInstituicao());
                    
                    professorRepository.save(professor);
                    atualizados++;
                    logger.debug("Professor atualizado: {} - {}", professor.getCpf(), professor.getNome());
                } else {
                    // Criar novo professor
                    professorRepository.save(professorArquivo);
                    criados++;
                    logger.debug("Professor criado: {} - {}", professorArquivo.getCpf(), professorArquivo.getNome());
                }
                
            } catch (Exception e) {
                logger.error("Erro ao processar professor: {}", professorArquivo.getCpf(), e);
            }
        }
        
        logger.info("Processamento finalizado: {} professores criados, {} atualizados", criados, atualizados);
    }

    private void criarArquivoExemplo() {
        try {
            File arquivo = new File(txtConfig.getPath());
            java.nio.file.Files.write(arquivo.toPath(), 
                """
                # Arquivo de professores - Formato: nome;cpf;email;password;departamento;instituicao
                # Comentários começam com #
                # A senha será criptografada automaticamente
                
                João Silva;123.456.789-00;joao.silva@email.com;senha123;Ciência da Computação;Universidade Federal
                Maria Santos;987.654.321-00;maria.santos@email.com;senha456;Matemática;Universidade Estadual
                Pedro Oliveira;111.222.333-44;pedro.oliveira@email.com;senha789;Física;Instituto Federal
                Ana Costa;555.666.777-88;ana.costa@email.com;senha012;Química;Universidade Privada
                """.getBytes(StandardCharsets.UTF_8));
            
            logger.info("Arquivo de exemplo criado: {}", txtConfig.getPath());
            
        } catch (Exception e) {
            logger.error("Erro ao criar arquivo de exemplo", e);
        }
    }

    // Método para processamento manual via API
    @Transactional
    public String processarArquivoManualmente() {
        try {
            File arquivo = new File(txtConfig.getPath());
            List<Professor> professores;
            
            if (arquivo.exists()) {
                professores = lerProfessoresDoArquivo(arquivo);
            } else {
                professores = lerProfessoresDoClasspath();
            }
            
            if (professores.isEmpty()) {
                return "Nenhum professor encontrado no arquivo ou arquivo não encontrado.";
            }
            
            processarProfessores(professores);
            
            return String.format("Processamento manual concluído. %d professores processados.", professores.size());
            
        } catch (Exception e) {
            logger.error("Erro no processamento manual", e);
            return "Erro no processamento: " + e.getMessage();
        }
    }
}