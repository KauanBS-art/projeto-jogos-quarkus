package com.kauangamestore.resource;

import com.kauangamestore.dto.AlterarSenhaDTO;
import com.kauangamestore.dto.PerfilUsuarioDTO;
import com.kauangamestore.dto.UsuarioDTO;
import com.kauangamestore.dto.UsuarioResponseDTO;
import com.kauangamestore.service.UsuarioService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.SecurityContext;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;
import jakarta.ws.rs.core.UriInfo;

@Path("/usuarios")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UsuarioResource {

    private static final int MAX_PAGE_SIZE = 100;

    @Inject
    UsuarioService service;

    @Inject
    SecurityContext securityContext;

    @GET
    @RolesAllowed("ADM")
    public Response buscarTodos(
        @QueryParam("page") @DefaultValue("0") int page,
        @QueryParam("pageSize") @DefaultValue("100") int pageSize
    ) {
        page = Math.max(0, page);
        pageSize = Math.min(Math.max(1, pageSize), MAX_PAGE_SIZE);

        return Response.ok(service.getAll(page, pageSize))
            .header("X-Page", page)
            .header("X-Page-Size", pageSize)
            .header("X-Total-Count", service.count())
            .build();
    }

    @GET
    @Path("/search/nome/{nome}")
    @RolesAllowed("ADM")
    public Response buscarPorNome(
        @PathParam("nome") String nome,
        @QueryParam("page") @DefaultValue("0") int page,
        @QueryParam("pageSize") @DefaultValue("100") int pageSize
    ) {
        page = Math.max(0, page);
        pageSize = Math.min(Math.max(1, pageSize), MAX_PAGE_SIZE);

        return Response.ok(service.findByNome(nome, page, pageSize))
            .header("X-Page", page)
            .header("X-Page-Size", pageSize)
            .header("X-Total-Count", service.countNome(nome))
            .build();
    }

    @GET
    @Path("/{id: \\d+}")
    @RolesAllowed("ADM")
    public Response buscarPorId(@PathParam("id") Long id) {
        return Response.ok(service.findById(id)).build();
    }

    @GET
    @Path("/me")
    @RolesAllowed({"USER", "ADM"})
    public Response usuarioLogado() {
        return Response.ok(service.findByEmail(emailLogado())).build();
    }

    @POST
    public Response incluir(@Valid UsuarioDTO dto, @Context UriInfo uriInfo) {
        UsuarioResponseDTO created = service.create(dto);
        return Response.created(
            UriBuilder.fromResource(UsuarioResource.class)
                .path(UsuarioResource.class, "buscarPorId")
                .build(created.id())
        ).entity(created).build();
    }

    @PUT
    @Path("/me")
    @RolesAllowed({"USER", "ADM"})
    public Response alterarPerfil(@Valid PerfilUsuarioDTO dto) {
        return Response.ok(service.updatePerfil(emailLogado(), dto)).build();
    }

    @PUT
    @Path("/me/senha")
    @RolesAllowed({"USER", "ADM"})
    public Response alterarSenha(@Valid AlterarSenhaDTO dto) {
        service.alterarSenha(emailLogado(), dto);
        return Response.noContent().build();
    }

    @GET
    @Path("/me/lista-desejos")
    @RolesAllowed({"USER", "ADM"})
    public Response listarDesejos() {
        return Response.ok(service.listarDesejos(emailLogado())).build();
    }

    @POST
    @Path("/me/lista-desejos/{idJogo}")
    @RolesAllowed({"USER", "ADM"})
    public Response adicionarDesejo(@PathParam("idJogo") Long idJogo) {
        return Response.ok(service.adicionarDesejo(emailLogado(), idJogo)).build();
    }

    @DELETE
    @Path("/me/lista-desejos/{idJogo}")
    @RolesAllowed({"USER", "ADM"})
    public Response removerDesejo(@PathParam("idJogo") Long idJogo) {
        return Response.ok(service.removerDesejo(emailLogado(), idJogo)).build();
    }

    @PUT
    @Path("/{id: \\d+}")
    @RolesAllowed("ADM")
    public Response alterar(@PathParam("id") Long id, @Valid UsuarioDTO dto) {
        return Response.ok(service.update(id, dto)).build();
    }

    @DELETE
    @Path("/{id: \\d+}")
    @RolesAllowed("ADM")
    public Response apagar(@PathParam("id") Long id) {
        service.delete(id);
        return Response.noContent().build();
    }

    @GET
    @Path("/count")
    @RolesAllowed("ADM")
    public long total() {
        return service.count();
    }

    @GET
    @Path("/me/cartoes")
    @RolesAllowed({"USER", "ADM"})
    public Response listarCartoes() {
        return Response.ok(service.listarCartoes(emailLogado())).build();
    }

    @POST
    @Path("/me/cartoes")
    @RolesAllowed({"USER", "ADM"})
    public Response adicionarCartao(@Valid com.kauangamestore.dto.CartaoCreditoDTO dto) {
        return Response.ok(service.adicionarCartao(emailLogado(), dto)).build();
    }

    @DELETE
    @Path("/me/cartoes/{id}")
    @RolesAllowed({"USER", "ADM"})
    public Response removerCartao(@PathParam("id") Long id) {
        service.removerCartao(emailLogado(), id);
        return Response.noContent().build();
    }

    private String emailLogado() {
        return securityContext.getUserPrincipal().getName();
    }
}
