package com.kauangamestore.dto;

import com.kauangamestore.model.Endereco;

public record EnderecoResponseDTO(
    Long id,
    String logradouro,
    String numero,
    String cep,
    String cidade,
    String bairro,
    String complemento,
    String estado
) {
    public static EnderecoResponseDTO valueOf(Endereco endereco) {
        if (endereco == null) {
            return null;
        }

        return new EnderecoResponseDTO(
            endereco.id,
            endereco.logradouro,
            endereco.numero,
            endereco.cep,
            endereco.cidade,
            endereco.bairro,
            endereco.complemento,
            endereco.estado
        );
    }
}
