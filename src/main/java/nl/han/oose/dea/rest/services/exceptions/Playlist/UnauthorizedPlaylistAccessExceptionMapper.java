package nl.han.oose.dea.rest.services.exceptions.Playlist;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class UnauthorizedPlaylistAccessExceptionMapper implements ExceptionMapper<UnauthorizedPlaylistAccessException> {
    @Override
    public Response toResponse(UnauthorizedPlaylistAccessException e) {
        return Response.status(Response.Status.FORBIDDEN)
                .entity(e.getMessage())
                .build();
    }
}