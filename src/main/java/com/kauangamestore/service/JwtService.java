package com.kauangamestore.service;

import com.kauangamestore.dto.UsuarioResponseDTO;

public interface JwtService {
    String generateJwt(UsuarioResponseDTO usuario);
}
