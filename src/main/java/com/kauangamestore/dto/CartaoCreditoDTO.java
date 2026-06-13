package com.kauangamestore.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record CartaoCreditoDTO(
    @NotBlank(message = "O número do cartão é obrigatório")
    @Pattern(regexp = "\\d{16}", message = "O cartão deve conter 16 dígitos")
    String numero,

    @NotBlank(message = "O nome do titular é obrigatório")
    String nomeTitular,

    @NotBlank(message = "A validade é obrigatória")
    @Pattern(regexp = "\\d{2}/\\d{2}", message = "Formato de validade inválido (MM/YY)")
    String validade,

    @NotBlank(message = "O CVV é obrigatório")
    @Pattern(regexp = "\\d{3,4}", message = "O CVV deve conter 3 ou 4 dígitos")
    String cvv
) {}
