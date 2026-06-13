package com.kauangamestore.dto;

import com.kauangamestore.model.Perfil;
import com.kauangamestore.model.Usuario;
import java.util.List;

public record UsuarioResponseDTO(
    Long id,
    String nome,
    String email,
    Perfil perfil,
    String telefone,
    String cpf,
    List<EnderecoResponseDTO> enderecos
) {
    public static UsuarioResponseDTO valueOf(Usuario usuario) {
        if (usuario == null) {
            return null;
        }

        return new UsuarioResponseDTO(
            usuario.getId(),
            usuario.getNome(),
            usuario.getEmail(),
            usuario.getPerfil(),
            usuario.getTelefone(),
            usuario.getCpf(),
            usuario.getEnderecos() == null
                ? List.of()
                : usuario.getEnderecos().stream().map(EnderecoResponseDTO::valueOf).toList()
        );
    }
}
