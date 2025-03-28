package nl.han.oose.dea.rest.resources.exceptionMappers;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import nl.han.oose.dea.rest.services.exceptions.InvalidTokenException;

@Provider
public class InvalidTokenExceptionMapper implements ExceptionMapper<InvalidTokenException> {
    @Override
    public Response toResponse(InvalidTokenException e) {
        return Response.status(Response.Status.UNAUTHORIZED)
                .entity(e.getMessage())
                .build();
    }
}
