package sistema.estudantil.system.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.transaction.annotation.Transactional;
import sistema.estudantil.system.repositories.VantagemRepository;
import sistema.estudantil.system.repositories.EmpresaRepository;
import sistema.estudantil.system.models.Vantagem;
import sistema.estudantil.system.models.Empresa;
import sistema.estudantil.system.dtos.VantagemDTO;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.lang.NonNull;

@Service
public class VantagemService {

    private static final List<String> TIPOS_PERMITIDOS = Arrays.asList("image/jpeg", "image/png", "image/webp");
    private static final long TAMANHO_MAXIMO_BYTES = 10 * 1024 * 1024; // 10MB

    @Autowired
    private VantagemRepository vantagemRepository;

    @Autowired
    private EmpresaRepository empresaRepository;

    // CREATE
    @Transactional
    public Vantagem createVantagem(String empresaCnpj, Vantagem vantagem, MultipartFile file) throws IOException {
        Empresa empresa = empresaRepository.findByCnpj(empresaCnpj)
                .orElseThrow(() -> new RuntimeException("Empresa não encontrada com CNPJ: " + empresaCnpj));

        // Processar a imagem se for fornecida
        if (file != null && !file.isEmpty()) {
            processarImagem(vantagem, file);
        }

        vantagem.setEmpresaDono(empresa);
        vantagem.setDataUpload(LocalDateTime.now());

        return vantagemRepository.save(vantagem);
    }

    // READ (One) - Retorna DTO
    @Transactional(readOnly = true)
    public Optional<VantagemDTO> getVantagemById(@NonNull Long id) {
        return vantagemRepository.findById(id)
                .map(this::toDTO);
    }

    // READ (All by Empresa) - Retorna DTOs
    @Transactional(readOnly = true)
    public List<VantagemDTO> getVantagensByEmpresa(String cnpj) {
        List<Vantagem> vantagens = vantagemRepository.findByEmpresaDonoCnpj(cnpj);
        return vantagens.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // READ (All) - Retorna DTOs
    @Transactional(readOnly = true)
    public List<VantagemDTO> getAllVantagens() {
        List<Vantagem> vantagens = vantagemRepository.findAll();
        return vantagens.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // Método auxiliar para converter Vantagem para VantagemDTO
    private VantagemDTO toDTO(Vantagem vantagem) {
        return new VantagemDTO(
                vantagem.getIdVantagem(),
                vantagem.getNome(),
                vantagem.getDescricao(),
                vantagem.getCusto(),
                vantagem.getCupom(),
                vantagem.getEmpresaDono() != null ? vantagem.getEmpresaDono().getNome() : null,
                vantagem.getEmpresaDono() != null ? vantagem.getEmpresaDono().getCnpj() : null,
                vantagem.getNomeArquivo(),
                vantagem.getTipoMime(),
                vantagem.getTamanhoBytes(),
                vantagem.getLargura(),
                vantagem.getAltura());
    }

    // UPDATE
    @Transactional
    public Vantagem updateVantagem(@NonNull Long id, Vantagem vantagemDetails, MultipartFile file) throws IOException {
        Vantagem vantagem = vantagemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vantagem não encontrada com ID: " + id));

        vantagem.setNome(vantagemDetails.getNome());
        vantagem.setDescricao(vantagemDetails.getDescricao());
        vantagem.setCusto(vantagemDetails.getCusto());
        vantagem.setCupom(vantagemDetails.getCupom());

        // Processar nova imagem se for fornecida
        if (file != null && !file.isEmpty()) {
            processarImagem(vantagem, file);
        }

        return vantagemRepository.save(vantagem);
    }

    // DELETE
    @Transactional
    public void deleteVantagem(@NonNull Long id) {
        if (!vantagemRepository.existsById(id)) {
            throw new RuntimeException("Vantagem não encontrada com ID: " + id);
        }
        vantagemRepository.deleteById(id);
    }

    // Método para processar e validar a imagem
    private void processarImagem(Vantagem vantagem, MultipartFile file) throws IOException {
        // Validar tipo de arquivo
        if (!TIPOS_PERMITIDOS.contains(file.getContentType())) {
            throw new IllegalArgumentException("Tipo de imagem não suportado. Use JPEG, PNG ou WebP.");
        }

        // Validar tamanho
        if (file.getSize() > TAMANHO_MAXIMO_BYTES) {
            throw new IllegalArgumentException("Tamanho máximo excedido (10MB).");
        }

        // Validar assinatura do arquivo
        byte[] dadosImagem = file.getBytes();
        String tipoReal = ImageTypeChecker.detectImageType(dadosImagem);
        if (!TIPOS_PERMITIDOS.contains(tipoReal)) {
            throw new IllegalArgumentException("Tipo de imagem não suportado detectado pela assinatura.");
        }

        // Processar dimensões da imagem
        Integer largura = null;
        Integer altura = null;
        try (var inputStream = file.getInputStream()) {
            var bufferedImage = javax.imageio.ImageIO.read(inputStream);
            if (bufferedImage != null) {
                largura = bufferedImage.getWidth();
                altura = bufferedImage.getHeight();
            }
        }

        // Configurar dados da imagem na vantagem
        vantagem.setDadosImagem(dadosImagem);
        vantagem.setNomeArquivo(file.getOriginalFilename());
        vantagem.setTipoMime(file.getContentType());
        vantagem.setTamanhoBytes(file.getSize());
        vantagem.setLargura(largura);
        vantagem.setAltura(altura);
    }

    @Transactional(readOnly = true)
    public Optional<Vantagem> getVantagemEntityById(@NonNull Long id) {
        // Use um método customizado no repository que force o carregamento dos dados da
        // imagem
        return vantagemRepository.findByIdWithImage(id);
    }

    @Transactional(readOnly = true)
    public List<Vantagem> getAllVantagensEntities() {
        return vantagemRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Vantagem> getVantagensByEmpresaEntities(String cnpj) {
        return vantagemRepository.findByEmpresaDonoCnpj(cnpj);
    }
}