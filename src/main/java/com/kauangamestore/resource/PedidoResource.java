package com.kauangamestore.resource;

import com.kauangamestore.dto.PedidoDTO;
import com.kauangamestore.service.PedidoService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;

@Path("/pedidos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PedidoResource {

    private static final int MAX_PAGE_SIZE = 100;

    @Inject
    PedidoService service;

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
    @Path("/{id: \\d+}")
    @RolesAllowed("ADM")
    public Response buscarPorId(@PathParam("id") Long id) {
        return Response.ok(service.findById(id)).build();
    }

    @POST
    @RolesAllowed({"USER", "ADM"})
    public Response incluir(@Valid PedidoDTO dto) {
        String email = securityContext.getUserPrincipal().getName();
        return Response.status(201).entity(service.create(dto, email)).build();
    }

    @GET
    @Path("/meus-pedidos")
    @RolesAllowed({"USER", "ADM"})
    public Response meusPedidos() {
        String email = securityContext.getUserPrincipal().getName();
        return Response.ok(service.findByUsuario(email)).build();
    }

    @GET
    @Path("/count")
    @RolesAllowed("ADM")
    public long total() {
        return service.count();
    }
}
