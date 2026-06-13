//Classe de Kauan Batista Silveira

package com.kauangamestore.exception;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class ValidationExceptionMapper implements ExceptionMapper<ValidationException> {

    @Override
    public Response toResponse(ValidationException exception) {
        Problem problem = new Problem(
            Response.Status.BAD_REQUEST.getStatusCode(),
            "ValidationException",
            "Erro de validação",
            exception.getMessage()
        );

        return Response.status(Response.Status.BAD_REQUEST).entity(problem).build();
    }
}