package com.kauangamestore.dto;

import com.kauangamestore.model.Empresa;

public record EmpresaResponseDTO(
    Long id,
    String nome,
    String paisOrigem,
    String descricao,
    int totalJogos
) {
    public static EmpresaResponseDTO valueOf(Empresa empresa) {
        int totalJogos = empresa.getJogos() == null ? 0 : empresa.getJogos().size();
        return new EmpresaResponseDTO(
            empresa.getId(),
            empresa.getNome(),
            empresa.getPaisOrigem(),
            empresa.getDescricao(),
            totalJogos
        );
    }
}
