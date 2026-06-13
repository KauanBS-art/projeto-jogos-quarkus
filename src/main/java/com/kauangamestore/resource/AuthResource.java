package com.kauangamestore.resource;

import com.kauangamestore.dto.AuthDTO;
import com.kauangamestore.dto.EsqueciSenhaDTO;
import com.kauangamestore.dto.UsuarioResponseDTO;
import com.kauangamestore.service.HashService;
import com.kauangamestore.service.JwtService;
import com.kauangamestore.service.UsuarioService;
import jakarta.annotation.security.PermitAll;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

@Path("/auth")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AuthResource {

    @Inject
    HashService hashService;

    @Inject
    UsuarioService usuarioService;

    @Inject
    JwtService jwtService;

    @POST
    @PermitAll
    public Response login(AuthDTO authDTO) {
        String hash = hashService.getHashSenha(authDTO.senha());
        UsuarioResponseDTO usuario = usuarioService.findByEmailAndSenha(authDTO.email(), hash);

        if (usuario == null) {
            return Response.status(Status.UNAUTHORIZED)
                .entity("Email ou senha invalidos")
                .build();
        }

        return Response.ok(usuario)
            .header("Authorization", jwtService.generateJwt(usuario))
            .build();
    }

    @POST
    @Path("/esqueci-senha")
    @PermitAll
    public Response esqueciSenha(EsqueciSenhaDTO dto) {
        usuarioService.solicitarRecuperacaoSenha(dto.email());
        return Response.noContent().build();
    }
}
