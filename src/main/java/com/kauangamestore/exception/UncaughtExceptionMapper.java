//Classe de Kauan Batista Silveira

package com.kauangamestore.exception;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class UncaughtExceptionMapper implements ExceptionMapper<Exception> {

    @Override
    public Response toResponse(Exception exception) {
        Problem problem = new Problem(
            Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(),
            "UncaughtException",
            "Erro interno no servidor",
            exception.getMessage()
        );

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(problem).build();
    }
}