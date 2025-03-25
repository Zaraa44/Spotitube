package nl.han.oose.dea.rest.services.exceptions.Track;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class TrackNotFoundExceptionMapper implements ExceptionMapper<TrackNotFoundException> {
    @Override
    public Response toResponse(TrackNotFoundException e) {
        return Response.status(Response.Status.NOT_FOUND)
                .entity(e.getMessage())
                .build();
    }
}