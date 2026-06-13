package com.kauangamestore.resource;

import com.kauangamestore.dto.JogoDTO;
import com.kauangamestore.dto.JogoResponseDTO;
import com.kauangamestore.service.JogoFileServiceImpl;
import com.kauangamestore.service.JogoService;
import java.io.File;
import java.io.IOException;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.jboss.resteasy.reactive.RestForm;
import org.jboss.resteasy.reactive.multipart.FileUpload;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.ResponseBuilder;
import jakarta.ws.rs.core.Response.Status;
import jakarta.ws.rs.core.UriBuilder;
import jakarta.ws.rs.core.UriInfo;

@Path("/jogos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class JogoResource {

    private static final int MAX_PAGE_SIZE = 100;

    @Inject
    JogoService service;

    @Inject
    JogoFileServiceImpl fileService;

    @GET
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
    @Path("/promocoes")
    public Response buscarPromocoes(
        @QueryParam("page") @DefaultValue("0") int page,
        @QueryParam("pageSize") @DefaultValue("100") int pageSize
    ) {
        page = Math.max(0, page);
        pageSize = Math.min(Math.max(1, pageSize), MAX_PAGE_SIZE);

        return Response.ok(service.findPromocoes(null, page, pageSize))
            .header("X-Page", page)
            .header("X-Page-Size", pageSize)
            .header("X-Total-Count", service.countPromocoes(null))
            .build();
    }

    @GET
    @Path("/promocoes/search/nome/{nome}")
    public Response buscarPromocoesPorNome(
        @PathParam("nome") String nome,
        @QueryParam("page") @DefaultValue("0") int page,
        @QueryParam("pageSize") @DefaultValue("100") int pageSize
    ) {
        page = Math.max(0, page);
        pageSize = Math.min(Math.max(1, pageSize), MAX_PAGE_SIZE);

        return Response.ok(service.findPromocoes(nome, page, pageSize))
            .header("X-Page", page)
            .header("X-Page-Size", pageSize)
            .header("X-Total-Count", service.countPromocoes(nome))
            .build();
    }

    @GET
    @Path("/{id: \\d+}")
    public Response buscarPorId(@PathParam("id") Long id) {
        return Response.ok(service.findById(id)).build();
    }

    @POST
    @RolesAllowed("ADM")
    public Response incluir(@Valid JogoDTO dto, @Context UriInfo uriInfo) {
        JogoResponseDTO created = service.create(dto);
        return Response.created(
            UriBuilder.fromResource(JogoResource.class)
                .path(JogoResource.class, "buscarPorId")
                .build(created.id())
        ).entity(created).build();
    }

    @PUT
    @Path("/{id: \\d+}")
    @RolesAllowed("ADM")
    public Response alterar(@PathParam("id") Long id, @Valid JogoDTO dto) {
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
    public long total() {
        return service.count();
    }

    @GET
    @Path("/image/download/{nomeImagem}")
    public Response download(@PathParam("nomeImagem") String nomeImagem) {
        File file = fileService.download(nomeImagem);
        ResponseBuilder response = Response.ok(file, mediaType(nomeImagem));
        response.header("Content-Disposition", "inline;filename=" + nomeImagem);
        return response.build();
    }

    @PATCH
    @Path("/image/upload")
    @RolesAllowed("ADM")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response salvarImagem(
        @RestForm("idJogo")
        @NotNull(message = "idJogo e obrigatorio.")
        @Min(value = 1, message = "idJogo deve ser maior ou igual a 1.")
        Long idJogo,

        @RestForm("file")
        @NotNull(message = "Arquivo de imagem e obrigatorio.")
        FileUpload file
    ) {
        try {
            fileService.salvar(idJogo, file);
            return Response.noContent().build();
        } catch (IOException e) {
            return Response.status(Status.CONFLICT).build();
        }
    }

    private String mediaType(String nomeImagem) {
        String nome = nomeImagem == null ? "" : nomeImagem.toLowerCase();
        if (nome.endsWith(".png")) {
            return "image/png";
        }
        if (nome.endsWith(".webp")) {
            return "image/webp";
        }
        if (nome.endsWith(".gif")) {
            return "image/gif";
        }
        return "image/jpeg";
    }
}
