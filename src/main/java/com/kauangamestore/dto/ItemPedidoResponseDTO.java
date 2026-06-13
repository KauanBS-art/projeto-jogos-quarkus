package com.kauangamestore.dto;

import com.kauangamestore.model.ItemPedido;

public record ItemPedidoResponseDTO(
    String tituloJogo,
    String nomeImagem,
    Integer quantidade,
    Double precoUnitario
) {
    public static ItemPedidoResponseDTO valueOf(ItemPedido item) {
        return new ItemPedidoResponseDTO(
            item.getJogo().getTitulo(),
            item.getJogo().getNomeImagem(),
            item.getQuantidade(),
            item.getPrecoUnitario()
        );
    }
}
