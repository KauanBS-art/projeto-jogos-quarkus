package com.kauangamestore.dto;

import com.kauangamestore.model.CartaoCredito;

public record CartaoCreditoResponseDTO(
    Long id,
    String numeroMascarado,
    String nomeTitular,
    String validade
) {
    public static CartaoCreditoResponseDTO valueOf(CartaoCredito cartao) {
        if (cartao == null) return null;
        String numero = cartao.getNumero();
        String mascarado = numero != null && numero.length() == 16 
            ? "**** **** **** " + numero.substring(12) 
            : "****";
        
        return new CartaoCreditoResponseDTO(
            cartao.getId(),
            mascarado,
            cartao.getNomeTitular(),
            cartao.getValidade()
        );
    }
}
