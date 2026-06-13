package com.kauangamestore.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record EsqueciSenhaDTO(
    @NotBlank(message = "O email e obrigatorio")
    @Email(message = "Email invalido")
    String email
) { }
