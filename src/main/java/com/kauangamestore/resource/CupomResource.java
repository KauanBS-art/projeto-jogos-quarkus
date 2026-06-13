package com.kauangamestore.resource;

import com.kauangamestore.dto.CupomDTO;
import com.kauangamestore.dto.CupomResponseDTO;
import com.kauangamestore.model.Cupom;
import com.kauangamestore.repository.CupomRepository;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

@Path("/cupons")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CupomResource {

    @Inject
    CupomRepository repository;

    @GET
    public List<CupomResponseDTO> listar() {
        return repository.findAll().stream()
            .map(CupomResponseDTO::valueOf)
            .collect(Collectors.toList());
    }

    @GET
    @Path("/{id}")
    public CupomResponseDTO buscar(@PathParam("id") Long id) {
        Cupom cupom = repository.findById(id);
        if (cupom == null) {
            throw new NotFoundException("Cupom não encontrado.");
        }
        return CupomResponseDTO.valueOf(cupom);
    }

    @GET
    @Path("/validar/{codigo}")
    public CupomResponseDTO validar(@PathParam("codigo") String codigo) {
        Cupom cupom = repository.findByCodigo(codigo);
        if (cupom == null || !Boolean.TRUE.equals(cupom.getAtivo())) {
            throw new NotFoundException("Cupom nao encontrado.");
        }

        return CupomResponseDTO.valueOf(cupom);
    }

    @POST
    @Transactional
    public Response criar(@Valid CupomDTO dto) {
        Cupom cupom = new Cupom();
        cupom.setDescricao(dto.descricao());
        cupom.setCodigo(dto.codigo());
        cupom.setPercentualDesconto(dto.percentualDesconto());
        cupom.setAtivo(dto.ativo() != null ? dto.ativo() : true);
        cupom.setRestricaoCategoria(dto.restricaoCategoria());
        cupom.setRestricaoPlataforma(dto.restricaoPlataforma());
        cupom.setRestricaoEmpresa(dto.restricaoEmpresa());
        cupom.setPrimeiraCompra(dto.primeiraCompra() != null ? dto.primeiraCompra() : false);
        cupom.setRestricaoPrecoMinimo(dto.restricaoPrecoMinimo());
        cupom.setRestricaoPrecoMaximo(dto.restricaoPrecoMaximo());
        
        repository.persist(cupom);
        return Response.status(Response.Status.CREATED).entity(CupomResponseDTO.valueOf(cupom)).build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public CupomResponseDTO atualizar(@PathParam("id") Long id, @Valid CupomDTO dto) {
        Cupom cupom = repository.findById(id);
        if (cupom == null) {
            throw new NotFoundException("Cupom não encontrado.");
        }

        cupom.setDescricao(dto.descricao());
        cupom.setCodigo(dto.codigo());
        cupom.setPercentualDesconto(dto.percentualDesconto());
        if (dto.ativo() != null) cupom.setAtivo(dto.ativo());
        cupom.setRestricaoCategoria(dto.restricaoCategoria());
        cupom.setRestricaoPlataforma(dto.restricaoPlataforma());
        cupom.setRestricaoEmpresa(dto.restricaoEmpresa());
        if (dto.primeiraCompra() != null) cupom.setPrimeiraCompra(dto.primeiraCompra());
        cupom.setRestricaoPrecoMinimo(dto.restricaoPrecoMinimo());
        cupom.setRestricaoPrecoMaximo(dto.restricaoPrecoMaximo());

        return CupomResponseDTO.valueOf(cupom);
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public Response remover(@PathParam("id") Long id) {
        Cupom cupom = repository.findById(id);
        if (cupom == null) {
            throw new NotFoundException("Cupom não encontrado.");
        }
        repository.delete(cupom);
        return Response.noContent().build();
    }
}
