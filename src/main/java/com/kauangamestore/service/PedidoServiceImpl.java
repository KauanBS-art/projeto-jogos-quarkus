package com.kauangamestore.service;

import com.kauangamestore.dto.ItemPedidoDTO;
import com.kauangamestore.dto.PedidoDTO;
import com.kauangamestore.dto.PedidoResponseDTO;
import com.kauangamestore.model.Endereco;
import com.kauangamestore.model.ItemPedido;
import com.kauangamestore.model.Jogo;
import com.kauangamestore.model.Pagamento;
import com.kauangamestore.model.PagamentoBoleto;
import com.kauangamestore.model.PagamentoPix;
import com.kauangamestore.model.Pedido;
import com.kauangamestore.model.Usuario;
import com.kauangamestore.model.Cupom;
import com.kauangamestore.repository.CupomRepository;
import com.kauangamestore.repository.JogoRepository;
import com.kauangamestore.repository.PedidoRepository;
import com.kauangamestore.repository.UsuarioRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotFoundException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.jboss.logging.Logger;

@ApplicationScoped
public class PedidoServiceImpl implements PedidoService {

    private static final Logger LOG = Logger.getLogger(PedidoServiceImpl.class);

    @Inject
    PedidoRepository pedidoRepository;

    @Inject
    UsuarioRepository usuarioRepository;

    @Inject
    JogoRepository jogoRepository;

    @Inject
    CupomRepository cupomRepository;

    @Inject
    Validator validator;

    @Override
    public List<PedidoResponseDTO> getAll(int page, int pageSize) {
        return pedidoRepository.findAll().page(page, pageSize).list().stream()
            .map(PedidoResponseDTO::valueOf)
            .toList();
    }

    @Override
    public long count() {
        return pedidoRepository.count();
    }

    @Override
    public PedidoResponseDTO findById(Long id) {
        Pedido pedido = pedidoRepository.findById(id);
        if (pedido == null) {
            throw new NotFoundException("Pedido nao encontrado.");
        }
        return PedidoResponseDTO.valueOf(pedido);
    }

    @Override
    @Transactional
    public PedidoResponseDTO create(PedidoDTO dto, String emailUsuario) {
        validar(dto);
        LOG.info("Iniciando pedido para o usuario: " + emailUsuario);

        Usuario usuario = usuarioRepository.findByEmail(emailUsuario);
        if (usuario == null) {
            throw new NotFoundException("Usuario nao encontrado.");
        }

        Pedido pedido = new Pedido();
        pedido.setDataPedido(LocalDateTime.now());
        pedido.setUsuario(usuario);
        pedido.setStatus(com.kauangamestore.model.StatusPedido.PROCESSANDO);
        pedido.setEnderecoEntrega(criarEndereco(dto, usuario));

        List<ItemPedido> itens = new ArrayList<>();
        double valorTotal = 0.0;

        for (ItemPedidoDTO itemDTO : dto.itens()) {
            Jogo jogo = jogoRepository.findById(itemDTO.idJogo());
            if (jogo == null) {
                throw new NotFoundException("Jogo nao encontrado: " + itemDTO.idJogo());
            }

            if (jogo.getEstoque() < itemDTO.quantidade()) {
                throw new BadRequestException("Estoque insuficiente: " + jogo.getTitulo());
            }

            jogo.setEstoque(jogo.getEstoque() - itemDTO.quantidade());

            ItemPedido item = new ItemPedido();
            item.setQuantidade(itemDTO.quantidade());
            item.setPrecoUnitario(jogo.getPreco());
            item.setJogo(jogo);
            item.setPedido(pedido);
            itens.add(item);

            valorTotal += jogo.getPreco() * itemDTO.quantidade();
        }

        double valorDesconto = arredondar(calcularDesconto(dto.codigoCupom(), itens, usuario));
        double valorFinal = arredondar(Math.max(0.0, valorTotal - valorDesconto));

        pedido.setItens(itens);
        pedido.setValorOriginal(valorTotal);
        pedido.setValorDesconto(valorDesconto);
        pedido.setValorTotal(valorFinal);
        pedido.setCodigoCupom(normalizarCupom(dto.codigoCupom()));
        pedido.setPagamento(criarPagamento(dto, valorFinal));

        pedidoRepository.persist(pedido);
        return PedidoResponseDTO.valueOf(pedido);
    }

    @Override
    public List<PedidoResponseDTO> findByUsuario(String emailUsuario) {
        return pedidoRepository.find("usuario.email", emailUsuario).list().stream()
            .map(PedidoResponseDTO::valueOf)
            .toList();
    }

    private void validar(PedidoDTO dto) {
        Set<ConstraintViolation<PedidoDTO>> violations = validator.validate(dto);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }

    private Endereco criarEndereco(PedidoDTO dto, Usuario usuario) {
        if (dto.idEndereco() != null) {
            return usuario.getEnderecos().stream()
                .filter(endereco -> dto.idEndereco().equals(endereco.id))
                .findFirst()
                .orElseThrow(() -> new BadRequestException("Endereco nao pertence ao usuario."));
        }

        Endereco endereco = new Endereco();
        endereco.cep = safeTrim(dto.cep());
        endereco.logradouro = safeTrim(dto.logradouro());
        endereco.numero = safeTrim(dto.numero());
        endereco.cidade = safeTrim(dto.cidade());
        endereco.bairro = safeTrim(dto.bairro());
        endereco.complemento = safeTrim(dto.complemento());
        endereco.estado = safeTrim(dto.estado());
        return endereco;
    }

    private double calcularDesconto(String codigoCupom, List<ItemPedido> itens, Usuario usuario) {
        String codigo = normalizarCupom(codigoCupom);
        if (codigo == null) {
            return 0.0;
        }

        Cupom cupom = cupomRepository.findByCodigo(codigo);
        if (cupom == null || !Boolean.TRUE.equals(cupom.getAtivo())) {
            throw new BadRequestException("Cupom invalido.");
        }
        
        if (Boolean.TRUE.equals(cupom.getPrimeiraCompra())) {
            long totalPedidos = pedidoRepository.count("usuario.email", usuario.getEmail());
            if (totalPedidos > 0) {
                throw new BadRequestException("Cupom valido apenas para a primeira compra.");
            }
        }

        double valorBaseDesconto = 0.0;
        for (ItemPedido item : itens) {
            boolean elegivel = true;
            Jogo jogo = item.getJogo();

            if (cupom.getRestricaoCategoria() != null && !cupom.getRestricaoCategoria().isBlank()) {
                if (jogo.getCategorias() == null || jogo.getCategorias().stream().noneMatch(c -> c.getNome().equalsIgnoreCase(cupom.getRestricaoCategoria()))) {
                    elegivel = false;
                }
            }
            if (cupom.getRestricaoPlataforma() != null && !cupom.getRestricaoPlataforma().isBlank()) {
                if (jogo.getPlataformas() == null || jogo.getPlataformas().stream().noneMatch(p -> p.getNome().equalsIgnoreCase(cupom.getRestricaoPlataforma()))) {
                    elegivel = false;
                }
            }
            if (cupom.getRestricaoEmpresa() != null && !cupom.getRestricaoEmpresa().isBlank()) {
                if (jogo.getDesenvolvedora() == null || !cupom.getRestricaoEmpresa().equalsIgnoreCase(jogo.getDesenvolvedora().getNome())) {
                    elegivel = false;
                }
            }
            if (cupom.getRestricaoPrecoMinimo() != null && jogo.getPreco() < cupom.getRestricaoPrecoMinimo()) {
                elegivel = false;
            }
            if (cupom.getRestricaoPrecoMaximo() != null && jogo.getPreco() > cupom.getRestricaoPrecoMaximo()) {
                elegivel = false;
            }

            if (!elegivel) {
                throw new BadRequestException("O cupom informado possui restrições incompatíveis com um ou mais itens do carrinho.");
            }

            valorBaseDesconto += (item.getPrecoUnitario() * item.getQuantidade());
        }

        if (valorBaseDesconto == 0.0) {
            throw new BadRequestException("O cupom não se aplica a nenhum dos itens selecionados no carrinho.");
        }

        double percentual = cupom.getPercentualDesconto() == null ? 0.0 : cupom.getPercentualDesconto();
        return valorBaseDesconto * (percentual / 100.0);
    }

    private String normalizarCupom(String codigoCupom) {
        if (codigoCupom == null || codigoCupom.isBlank()) {
            return null;
        }

        return codigoCupom.trim().toUpperCase();
    }

    private double arredondar(double valor) {
        return Math.round(valor * 100.0) / 100.0;
    }

    private Pagamento criarPagamento(PedidoDTO dto, double valorTotal) {
        Pagamento pagamento;
        Integer idModoPagamento = dto.idModoPagamento();

        if (Integer.valueOf(1).equals(idModoPagamento)) {
            PagamentoPix pix = new PagamentoPix();
            pix.setDataExpiracao(LocalDateTime.now().plusMinutes(30));
            pix.setChavePix(UUID.randomUUID().toString());
            pix.setCodigoTransacao(UUID.randomUUID().toString());
            pagamento = pix;
        } else if (Integer.valueOf(3).equals(idModoPagamento)) {
            if (dto.idCartaoCredito() == null) {
                throw new BadRequestException("Cartao de credito nao informado.");
            }
            com.kauangamestore.model.CartaoCredito cartao = com.kauangamestore.model.CartaoCredito.findById(dto.idCartaoCredito());
            if (cartao == null) {
                throw new BadRequestException("Cartao de credito invalido.");
            }
            com.kauangamestore.model.PagamentoCartao pagCartao = new com.kauangamestore.model.PagamentoCartao();
            pagCartao.setCartao(cartao);
            pagCartao.setParcelas(1); // Fixo em 1x por enquanto
            pagamento = pagCartao;
        } else {
            PagamentoBoleto boleto = new PagamentoBoleto();
            boleto.setDataVencimento(LocalDate.now().plusDays(3));
            boleto.setNumeroBoleto("34191.79001 " + System.currentTimeMillis());
            pagamento = boleto;
        }

        pagamento.setValor(valorTotal);
        pagamento.setDataConfirmacao(null);
        return pagamento;
    }

    private String safeTrim(String value) {
        return value == null ? null : value.trim();
    }
}
