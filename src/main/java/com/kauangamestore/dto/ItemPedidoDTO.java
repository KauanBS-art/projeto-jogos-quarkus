package com.kauangamestore.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ItemPedidoDTO(
    @NotNull(message = "O ID do jogo é obrigatório")
    Long idJogo,

    @NotNull(message = "A quantidade é obrigatória")
    @Positive(message = "A quantidade deve ser maior que zero")
    Integer quantidade
) { }