package com.kauangamestore.dto;

import jakarta.validation.constraints.NotBlank;

public record EnderecoDTO(
    Long id,

    @NotBlank(message = "O logradouro e obrigatorio")
    String logradouro,

    @NotBlank(message = "O numero e obrigatorio")
    String numero,

    @NotBlank(message = "O CEP e obrigatorio")
    String cep,

    @NotBlank(message = "A cidade e obrigatoria")
    String cidade,

    String bairro,
    String complemento,
    String estado
) { }
