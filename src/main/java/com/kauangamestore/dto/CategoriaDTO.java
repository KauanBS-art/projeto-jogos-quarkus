//Classe de Kauan Batista Silveira

package com.kauangamestore.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CategoriaDTO(
    @NotBlank(message = "O nome da categoria deve ser informado.")
    @Size(min = 2, max = 100, message = "O nome da categoria deve ter entre 2 e 100 caracteres.")
    String nome
) {}
