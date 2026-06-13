package com.kauangamestore.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.util.List;

public record PerfilUsuarioDTO(
    @NotBlank(message = "O nome e obrigatorio")
    String nome,

    @NotBlank(message = "O email e obrigatorio")
    @Email(message = "Email invalido")
    String email,

    String telefone,
    String cpf,

    @NotBlank(message = "Informe a senha para confirmar a alteracao")
    String senhaConfirmacao,

    @Valid
    List<EnderecoDTO> enderecos
) { }
