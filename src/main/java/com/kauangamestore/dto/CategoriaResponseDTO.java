package com.kauangamestore.dto;

import com.kauangamestore.model.Categoria;

public record CategoriaResponseDTO(
    Long id,
    String nome,
    int totalJogos
) {
    public static CategoriaResponseDTO valueOf(Categoria categoria) {
        int totalJogos = categoria.getJogos() == null ? 0 : categoria.getJogos().size();
        return new CategoriaResponseDTO(categoria.getId(), categoria.getNome(), totalJogos);
    }
}
