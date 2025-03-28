package nl.han.oose.dea.rest.resources;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import nl.han.oose.dea.rest.datasource.dao.TrackDAO;
import nl.han.oose.dea.rest.datasource.dao.UserDAO;
import nl.han.oose.dea.rest.datasource.dao.PlaylistDAO;
import nl.han.oose.dea.rest.services.dto.track.TracksResponseDTO;
import nl.han.oose.dea.rest.services.dto.track.TrackDTO;

import java.util.List;

@Path("/tracks")
public class TrackResource {

    @Inject
    private PlaylistDAO playlistDAO;

    @Inject
    private TrackDAO trackDAO;

    @Inject
    private UserDAO userDAO;

    public TrackResource() {}

    public TrackResource(TrackDAO trackDAO, UserDAO userDAO) {
        this.trackDAO = trackDAO;
        this.userDAO = userDAO;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAvailableTracks(@QueryParam("forPlaylist") int playlistId,
                                       @QueryParam("token") String token) {

        String username = userDAO.getUsernameByToken(token);
        if (username == null) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("Invalid token").build();
        }

        List<TrackDTO> tracks = trackDAO.getTracksNotInPlaylist(playlistId);
        return Response.ok(new TracksResponseDTO(tracks)).build();
    }
}
