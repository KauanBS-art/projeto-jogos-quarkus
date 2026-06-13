package com.kauangamestore.dto;

import jakarta.validation.constraints.NotNull;
import java.util.List;

public record PedidoDTO(
    @NotNull(message = "A lista de itens não pode ser nula")
    List<ItemPedidoDTO> itens,

    @NotNull(message = "O modo de pagamento é obrigatório (1=PIX, 2=BOLETO)")
    Integer idModoPagamento,

    String cep,
    String logradouro,
    String numero,
    String cidade,
    String bairro,
    String complemento,
    String estado,
    Long idEndereco,
    String codigoCupom,
    Long idCartaoCredito
) {
    public PedidoDTO(
        List<ItemPedidoDTO> itens,
        Integer idModoPagamento,
        String cep,
        String logradouro,
        String numero,
        String cidade
    ) {
        this(itens, idModoPagamento, cep, logradouro, numero, cidade, null, null, null, null, null, null);
    }
}
