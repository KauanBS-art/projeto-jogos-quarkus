package com.kauangamestore.service;

import com.kauangamestore.dto.AlterarSenhaDTO;
import com.kauangamestore.dto.JogoResponseDTO;
import com.kauangamestore.dto.PerfilUsuarioDTO;
import com.kauangamestore.dto.UsuarioDTO;
import com.kauangamestore.dto.UsuarioResponseDTO;
import java.util.List;

public interface UsuarioService {

    List<UsuarioResponseDTO> getAll(int page, int pageSize);

    UsuarioResponseDTO findById(Long id);

    List<UsuarioResponseDTO> findByNome(String nome, int page, int pageSize);

    long count();

    long countNome(String nome);

    UsuarioResponseDTO create(UsuarioDTO dto);

    UsuarioResponseDTO update(Long id, UsuarioDTO dto);

    void delete(Long id);

    UsuarioResponseDTO findByEmailAndSenha(String email, String senha);

    UsuarioResponseDTO findByEmail(String email);

    UsuarioResponseDTO updatePerfil(String email, PerfilUsuarioDTO dto);

    void alterarSenha(String email, AlterarSenhaDTO dto);

    void solicitarRecuperacaoSenha(String email);

    List<JogoResponseDTO> listarDesejos(String email);

    List<JogoResponseDTO> adicionarDesejo(String email, Long idJogo);

    List<JogoResponseDTO> removerDesejo(String email, Long idJogo);
    List<com.kauangamestore.dto.CartaoCreditoResponseDTO> listarCartoes(String email);
    com.kauangamestore.dto.CartaoCreditoResponseDTO adicionarCartao(String email, com.kauangamestore.dto.CartaoCreditoDTO dto);
    void removerCartao(String email, Long idCartao);
}
