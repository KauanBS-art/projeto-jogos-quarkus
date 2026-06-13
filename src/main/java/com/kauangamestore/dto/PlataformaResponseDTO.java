package com.kauangamestore.dto;

import com.kauangamestore.model.Plataforma;

public record PlataformaResponseDTO(
    Long id,
    String nome,
    int totalJogos
) {
    public static PlataformaResponseDTO valueOf(Plataforma plataforma) {
        int totalJogos = plataforma.getJogos() == null ? 0 : plataforma.getJogos().size();
        return new PlataformaResponseDTO(plataforma.getId(), plataforma.getNome(), totalJogos);
    }
}
