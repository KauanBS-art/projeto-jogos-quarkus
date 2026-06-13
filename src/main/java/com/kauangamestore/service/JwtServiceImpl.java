package com.kauangamestore.service;

import com.kauangamestore.dto.UsuarioResponseDTO;
import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;
import java.time.Duration;
import java.time.Instant;
import java.util.Set;

@ApplicationScoped
public class JwtServiceImpl implements JwtService {

    private static final Duration EXPIRATION_TIME = Duration.ofHours(24);

    @Override
    public String generateJwt(UsuarioResponseDTO usuario) {
        Instant expiryDate = Instant.now().plus(EXPIRATION_TIME);

        return Jwt.issuer("kauan-games-jwt")
            .subject(usuario.email())
            .groups(Set.of(usuario.perfil().name()))
            .claim("id", usuario.id())
            .expiresAt(expiryDate)
            .sign();
    }
}
