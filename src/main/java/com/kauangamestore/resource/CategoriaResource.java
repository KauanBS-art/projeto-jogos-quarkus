package com.kauangamestore.resource;

import com.kauangamestore.dto.CategoriaDTO;
import com.kauangamestore.dto.CategoriaResponseDTO;
import com.kauangamestore.service.CategoriaService;
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
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;
import jakarta.ws.rs.core.UriInfo;

@Path("/categorias")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CategoriaResource {

    private static final int MAX_PAGE_SIZE = 100;

    @Inject
    CategoriaService service;

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

    @POST
    @RolesAllowed("ADM")
    public Response incluir(@Valid CategoriaDTO dto, @Context UriInfo uriInfo) {
        CategoriaResponseDTO created = service.create(dto);
        return Response.created(
            UriBuilder.fromResource(CategoriaResource.class)
                .path(CategoriaResource.class, "buscarPorId")
                .build(created.id())
        ).entity(created).build();
    }

    @PUT
    @Path("/{id: \\d+}")
    @RolesAllowed("ADM")
    public Response alterar(@PathParam("id") Long id, @Valid CategoriaDTO dto) {
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
}
