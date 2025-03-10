package nl.han.oose.dea.resources.exceptions;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import nl.han.oose.dea.services.exceptions.ItemNotAvailableException;

@Provider
public class ItemNotAvailableExceptionMapper implements ExceptionMapper<ItemNotAvailableException> {
    public Response toResponse(ItemNotAvailableException ex) {
        return Response.status(404).
                entity(ex.getMessage()).
                type("text/plain").
                build();
    }
}
