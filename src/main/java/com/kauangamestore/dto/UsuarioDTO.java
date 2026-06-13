//Classe de Kauan Batista Silveira

package com.kauangamestore.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UsuarioDTO(
    @NotBlank(message = "O nome é obrigatório")
    String nome,

    @NotBlank(message = "O email é obrigatório")
    @Email(message = "Email inválido")
    String email,

    @Size(min = 6, message = "A senha deve ter pelo menos 6 caracteres")
    String senha,

    String telefone,
    String cpf,

    Integer idPerfil
) {
    public UsuarioDTO(String nome, String email, String senha, Integer idPerfil) {
        this(nome, email, senha, null, null, idPerfil);
    }
}
