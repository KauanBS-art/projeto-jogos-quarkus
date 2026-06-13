package com.kauangamestore.dto;

import com.kauangamestore.model.Pedido;
import java.time.LocalDateTime;
import java.util.List;

public record PedidoResponseDTO(
    Long id,
    LocalDateTime dataPedido,
    Double valorOriginal,
    Double valorDesconto,
    Double valorTotal,
    String codigoCupom,
    String modoPagamento,
    String status,
    String usuarioEmail,
    EnderecoResponseDTO enderecoEntrega,
    List<ItemPedidoResponseDTO> itens
) {
    public static PedidoResponseDTO valueOf(Pedido pedido) {
        String tipoPagamento = pedido.getPagamento().getClass().getSimpleName()
            .replace("Pagamento", "")
            .toUpperCase();

        return new PedidoResponseDTO(
            pedido.getId(),
            pedido.getDataPedido(),
            pedido.getValorOriginal(),
            pedido.getValorDesconto(),
            pedido.getValorTotal(),
            pedido.getCodigoCupom(),
            tipoPagamento,
            pedido.getStatus() != null ? pedido.getStatus().name() : "PROCESSANDO",
            pedido.getUsuario() == null ? null : pedido.getUsuario().getEmail(),
            EnderecoResponseDTO.valueOf(pedido.getEnderecoEntrega()),
            pedido.getItens().stream().map(ItemPedidoResponseDTO::valueOf).toList()
        );
    }
}
