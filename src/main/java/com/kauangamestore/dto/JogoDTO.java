package com.kauangamestore.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDate;
import java.util.List;

public record JogoDTO(
    @NotBlank(message = "Titulo e obrigatorio")
    String titulo,

    String descricao,

    @NotNull(message = "Preco e obrigatorio")
    @Positive(message = "Preco deve ser maior que zero")
    Double preco,

    @NotNull(message = "Estoque e obrigatorio")
    @Positive(message = "Estoque deve ser maior que zero")
    Integer estoque,

    Double percentualDesconto,

    LocalDate dataLancamento,

    @NotNull(message = "Empresa e obrigatoria")
    Long idEmpresa,

    List<Long> idPlataformas,

    List<Long> idCategorias
) {
}
