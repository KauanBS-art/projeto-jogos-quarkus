package com.kauangamestore.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CupomDTO(
    String descricao,
    @NotBlank(message = "O código é obrigatório")
    String codigo,
    @NotNull(message = "O percentual de desconto é obrigatório")
    @Positive(message = "O percentual deve ser positivo")
    Double percentualDesconto,
    Boolean ativo,
    String restricaoCategoria,
    String restricaoPlataforma,
    String restricaoEmpresa,
    Boolean primeiraCompra,
    Double restricaoPrecoMinimo,
    Double restricaoPrecoMaximo
) {}
