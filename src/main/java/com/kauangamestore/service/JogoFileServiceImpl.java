package com.kauangamestore.service;

import com.kauangamestore.model.Jogo;
import com.kauangamestore.repository.JogoRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;
import org.jboss.resteasy.reactive.multipart.FileUpload;

@ApplicationScoped
public class JogoFileServiceImpl implements FileService {

    private static final Path JOGO_UPLOAD_DIR = Paths.get(
        System.getProperty("user.home"),
        "quarkus",
        "images",
        "jogo"
    );

    private static final Set<String> ALLOWED_EXTENSIONS = Set.of("jpg", "jpeg", "png", "webp", "gif");
    private static final long MAX_FILE_SIZE = 5L * 1024 * 1024;
    private static final long MIN_FILE_SIZE = 1L;

    @Inject
    JogoRepository jogoRepository;

    @Override
    @Transactional
    public void salvar(Long id, FileUpload file) throws IOException {
        Jogo jogo = jogoRepository.findById(id);
        if (jogo == null) {
            throw new NotFoundException("Jogo nao encontrado.");
        }

        String imagemAnterior = jogo.getNomeImagem();
        String novoNomeImagem = salvarImagem(file);
        jogo.setNomeImagem(novoNomeImagem);
        excluirImagemAnterior(imagemAnterior, novoNomeImagem);
    }

    private String salvarImagem(FileUpload file) {
        if (file == null || file.uploadedFile() == null) {
            throw new WebApplicationException("Arquivo de imagem nao informado.", Response.Status.BAD_REQUEST);
        }

        try {
            validarTamanho(file);
            validarExtensao(file);

            Files.createDirectories(JOGO_UPLOAD_DIR);

            String novoNome = gerarNomeAleatorio(file.fileName());
            Path destino = JOGO_UPLOAD_DIR.resolve(novoNome);
            Files.copy(file.uploadedFile(), destino, StandardCopyOption.REPLACE_EXISTING);

            return novoNome;
        } catch (IOException e) {
            throw new WebApplicationException("Erro ao salvar a imagem.", e, Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    private void validarTamanho(FileUpload file) {
        long size = file.size();

        if (size <= 0) {
            throw new WebApplicationException("Arquivo vazio.", Response.Status.BAD_REQUEST);
        }

        if (size < MIN_FILE_SIZE) {
            throw new WebApplicationException("Arquivo muito pequeno para ser considerado uma imagem valida.", Response.Status.BAD_REQUEST);
        }

        if (size > MAX_FILE_SIZE) {
            throw new WebApplicationException("Arquivo muito grande. Tamanho maximo permitido: 5 MB.", Response.Status.BAD_REQUEST);
        }
    }

    private void validarExtensao(FileUpload file) {
        String ext = getExtension(file.fileName());

        if (ext == null || !ALLOWED_EXTENSIONS.contains(ext.toLowerCase(Locale.ROOT))) {
            throw new WebApplicationException("Extensao de arquivo nao suportada.", Response.Status.BAD_REQUEST);
        }
    }

    private String getExtension(String fileName) {
        if (fileName == null) {
            return null;
        }

        String onlyName = Paths.get(fileName).getFileName().toString();
        int idx = onlyName.lastIndexOf('.');
        if (idx == -1 || idx == onlyName.length() - 1) {
            return null;
        }

        return onlyName.substring(idx + 1);
    }

    private String gerarNomeAleatorio(String originalName) {
        String onlyName = Paths.get(originalName).getFileName().toString();
        String ext = "";
        int idx = onlyName.lastIndexOf('.');
        if (idx != -1) {
            ext = onlyName.substring(idx).toLowerCase(Locale.ROOT);
        }

        return UUID.randomUUID() + ext;
    }

    private void excluirImagemAnterior(String imagemAnterior, String novoNomeImagem) {
        if (imagemAnterior == null || imagemAnterior.equals(novoNomeImagem)) {
            return;
        }

        try {
            Files.deleteIfExists(JOGO_UPLOAD_DIR.resolve(imagemAnterior));
        } catch (IOException ignored) {
            // A troca da capa nao deve falhar se a imagem antiga ja nao existir.
        }
    }

    @Override
    public File download(String nomeArquivo) {
        File file = JOGO_UPLOAD_DIR.resolve(nomeArquivo).toFile();
        if (!file.exists() || !file.isFile()) {
            throw new NotFoundException("Imagem nao encontrada.");
        }

        return file;
    }
}
