package com.kauangamestore.dto;

import com.kauangamestore.model.Cupom;

public record CupomResponseDTO(
    Long id,
    String descricao,
    String codigo,
    Double percentualDesconto,
    Boolean ativo,
    String restricaoCategoria,
    String restricaoPlataforma,
    String restricaoEmpresa,
    Boolean primeiraCompra,
    Double restricaoPrecoMinimo,
    Double restricaoPrecoMaximo
) {
    public static CupomResponseDTO valueOf(Cupom cupom) {
        if (cupom == null) {
            return null;
        }

        return new CupomResponseDTO(
            cupom.getId(),
            cupom.getDescricao(),
            cupom.getCodigo(),
            cupom.getPercentualDesconto(),
            cupom.getAtivo() != null ? cupom.getAtivo() : true,
            cupom.getRestricaoCategoria(),
            cupom.getRestricaoPlataforma(),
            cupom.getRestricaoEmpresa(),
            cupom.getPrimeiraCompra(),
            cupom.getRestricaoPrecoMinimo(),
            cupom.getRestricaoPrecoMaximo()
        );
    }
}
