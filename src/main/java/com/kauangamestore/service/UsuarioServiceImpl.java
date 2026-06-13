package com.kauangamestore.service;

import com.kauangamestore.dto.AlterarSenhaDTO;
import com.kauangamestore.dto.EnderecoDTO;
import com.kauangamestore.dto.JogoResponseDTO;
import com.kauangamestore.dto.PerfilUsuarioDTO;
import com.kauangamestore.dto.UsuarioDTO;
import com.kauangamestore.dto.UsuarioResponseDTO;
import com.kauangamestore.model.Endereco;
import com.kauangamestore.model.Jogo;
import com.kauangamestore.model.Perfil;
import com.kauangamestore.model.Usuario;
import com.kauangamestore.repository.JogoRepository;
import com.kauangamestore.repository.UsuarioRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.ForbiddenException;
import jakarta.ws.rs.NotFoundException;
import org.jboss.logging.Logger;
import java.util.List;
import java.util.Set;

@ApplicationScoped
public class UsuarioServiceImpl implements UsuarioService {

    private static final Logger LOG = Logger.getLogger(UsuarioServiceImpl.class);

    @Inject
    UsuarioRepository repository;

    @Inject
    JogoRepository jogoRepository;

    @Inject
    HashService hashService;

    @Inject
    Validator validator;

    @Override
    public List<UsuarioResponseDTO> getAll(int page, int pageSize) {
        return repository.findAll().page(page, pageSize).list().stream()
            .map(UsuarioResponseDTO::valueOf)
            .toList();
    }

    @Override
    public UsuarioResponseDTO findById(Long id) {
        Usuario usuario = repository.findById(id);
        if (usuario == null) {
            throw new NotFoundException("Usuario nao encontrado.");
        }
        return UsuarioResponseDTO.valueOf(usuario);
    }

    @Override
    public List<UsuarioResponseDTO> findByNome(String nome, int page, int pageSize) {
        return repository.findByNome(nome).page(page, pageSize).list().stream()
            .map(UsuarioResponseDTO::valueOf)
            .toList();
    }

    @Override
    public long count() {
        return repository.count();
    }

    @Override
    public long countNome(String nome) {
        return repository.countNome(nome);
    }

    @Override
    @Transactional
    public UsuarioResponseDTO create(UsuarioDTO dto) {
        validar(dto);
        Usuario entity = new Usuario();
        apply(dto, entity, true);
        repository.persist(entity);
        return UsuarioResponseDTO.valueOf(entity);
    }

    @Override
    @Transactional
    public UsuarioResponseDTO update(Long id, UsuarioDTO dto) {
        validar(dto);
        Usuario entity = repository.findById(id);
        if (entity == null) {
            throw new NotFoundException("Usuario nao encontrado.");
        }
        apply(dto, entity, false);
        entity.persist();
        return UsuarioResponseDTO.valueOf(entity);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!repository.deleteById(id)) {
            throw new NotFoundException("Usuario nao encontrado.");
        }
    }

    @Override
    public UsuarioResponseDTO findByEmailAndSenha(String email, String senha) {
        Usuario usuario = repository.findByEmailAndSenha(email, senha);
        return UsuarioResponseDTO.valueOf(usuario);
    }

    @Override
    public UsuarioResponseDTO findByEmail(String email) {
        return UsuarioResponseDTO.valueOf(findEntityByEmail(email));
    }

    @Override
    @Transactional
    public UsuarioResponseDTO updatePerfil(String email, PerfilUsuarioDTO dto) {
        Usuario usuario = findEntityByEmail(email);
        validarSenha(usuario, dto.senhaConfirmacao());

        usuario.setNome(safeTrim(dto.nome()));
        usuario.setEmail(safeTrim(dto.email()));
        usuario.setTelefone(safeTrim(dto.telefone()));
        usuario.setCpf(safeTrim(dto.cpf()));

        usuario.getEnderecos().clear();
        if (dto.enderecos() != null) {
            dto.enderecos().stream()
                .map(this::toEndereco)
                .forEach(usuario.getEnderecos()::add);
        }

        return UsuarioResponseDTO.valueOf(usuario);
    }

    @Override
    @Transactional
    public void alterarSenha(String email, AlterarSenhaDTO dto) {
        Usuario usuario = findEntityByEmail(email);
        validarSenha(usuario, dto.senhaAtual());
        usuario.setSenha(hashService.getHashSenha(dto.novaSenha()));
    }

    @Override
    public void solicitarRecuperacaoSenha(String email) {
        Usuario usuario = repository.findByEmail(email);
        if (usuario != null) {
            LOG.info("Solicitacao de recuperacao de senha para: " + email);
        }
    }

    @Override
    public List<JogoResponseDTO> listarDesejos(String email) {
        Usuario usuario = findEntityByEmail(email);
        return usuario.getListaDesejos().stream().map(JogoResponseDTO::valueOf).toList();
    }

    @Override
    @Transactional
    public List<JogoResponseDTO> adicionarDesejo(String email, Long idJogo) {
        Usuario usuario = findEntityByEmail(email);
        Jogo jogo = jogoRepository.findById(idJogo);
        if (jogo == null) {
            throw new NotFoundException("Jogo nao encontrado.");
        }

        boolean jaExiste = usuario.getListaDesejos().stream()
            .anyMatch(item -> item.getId().equals(idJogo));

        if (!jaExiste) {
            usuario.getListaDesejos().add(jogo);
        }

        return listarDesejos(email);
    }

    @Override
    @Transactional
    public List<JogoResponseDTO> removerDesejo(String email, Long idJogo) {
        Usuario usuario = findEntityByEmail(email);
        usuario.getListaDesejos().removeIf(jogo -> jogo.getId().equals(idJogo));
        return usuario.getListaDesejos().stream().map(JogoResponseDTO::valueOf).toList();
    }

    private void validar(UsuarioDTO dto) {
        Set<ConstraintViolation<UsuarioDTO>> violations = validator.validate(dto);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }

    private void apply(UsuarioDTO dto, Usuario entity, boolean createMode) {
        entity.setNome(safeTrim(dto.nome()));
        entity.setEmail(safeTrim(dto.email()));
        entity.setTelefone(safeTrim(dto.telefone()));
        entity.setCpf(safeTrim(dto.cpf()));

        if (createMode) {
            if (dto.senha() == null || dto.senha().trim().length() < 6) {
                throw new jakarta.ws.rs.BadRequestException("A senha deve ter pelo menos 6 caracteres.");
            }
            entity.setSenha(hashService.getHashSenha(dto.senha()));
        } else if (dto.senha() != null && !dto.senha().isBlank()) {
            if (dto.senha().trim().length() < 6) {
                throw new jakarta.ws.rs.BadRequestException("A senha deve ter pelo menos 6 caracteres.");
            }
            entity.setSenha(hashService.getHashSenha(dto.senha()));
        }

        Integer idPerfil = dto.idPerfil() == null ? Perfil.USER.getId() : dto.idPerfil();
        entity.setPerfil(Perfil.valueOf(idPerfil));
    }

    private String safeTrim(String value) {
        return value == null ? null : value.trim();
    }

    private Usuario findEntityByEmail(String email) {
        Usuario usuario = repository.findByEmail(email);
        if (usuario == null) {
            throw new NotFoundException("Usuario nao encontrado.");
        }
        return usuario;
    }

    private void validarSenha(Usuario usuario, String senha) {
        if (senha == null || senha.isBlank()) {
            throw new BadRequestException("Senha de confirmacao obrigatoria.");
        }

        String hash = hashService.getHashSenha(senha);
        if (!hash.equals(usuario.getSenha())) {
            throw new ForbiddenException("Senha invalida.");
        }
    }

    private Endereco toEndereco(EnderecoDTO dto) {
        Endereco endereco = new Endereco();
        endereco.logradouro = safeTrim(dto.logradouro());
        endereco.numero = safeTrim(dto.numero());
        endereco.cep = safeTrim(dto.cep());
        endereco.cidade = safeTrim(dto.cidade());
        endereco.bairro = safeTrim(dto.bairro());
        endereco.complemento = safeTrim(dto.complemento());
        endereco.estado = safeTrim(dto.estado());
        return endereco;
    }
    @Override
    public List<com.kauangamestore.dto.CartaoCreditoResponseDTO> listarCartoes(String email) {
        Usuario usuario = repository.findByEmail(email);
        if (usuario == null) throw new NotFoundException("Usuario nao encontrado.");
        return com.kauangamestore.model.CartaoCredito.<com.kauangamestore.model.CartaoCredito>find("usuario", usuario).list()
            .stream().map(com.kauangamestore.dto.CartaoCreditoResponseDTO::valueOf).toList();
    }

    @Override
    @Transactional
    public com.kauangamestore.dto.CartaoCreditoResponseDTO adicionarCartao(String email, com.kauangamestore.dto.CartaoCreditoDTO dto) {
        Usuario usuario = repository.findByEmail(email);
        if (usuario == null) throw new NotFoundException("Usuario nao encontrado.");

        com.kauangamestore.model.CartaoCredito cartao = new com.kauangamestore.model.CartaoCredito();
        cartao.setNumero(dto.numero());
        cartao.setNomeTitular(dto.nomeTitular());
        cartao.setValidade(dto.validade());
        cartao.setCvv(dto.cvv());
        cartao.setUsuario(usuario);
        
        cartao.persist();
        return com.kauangamestore.dto.CartaoCreditoResponseDTO.valueOf(cartao);
    }

    @Override
    @Transactional
    public void removerCartao(String email, Long idCartao) {
        Usuario usuario = repository.findByEmail(email);
        if (usuario == null) throw new NotFoundException("Usuario nao encontrado.");

        com.kauangamestore.model.CartaoCredito cartao = com.kauangamestore.model.CartaoCredito.findById(idCartao);
        if (cartao == null || !cartao.getUsuario().getId().equals(usuario.getId())) {
            throw new NotFoundException("Cartao nao encontrado.");
        }
        
        cartao.delete();
    }
}
