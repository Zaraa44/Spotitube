package nl.han.oose.dea.rest.resources;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import nl.han.oose.dea.rest.datasource.data.TrackDAO;
import nl.han.oose.dea.rest.datasource.data.UserDAO;
import nl.han.oose.dea.rest.datasource.data.PlaylistDAO;
import nl.han.oose.dea.rest.services.dto.Track.TracksResponseDTO;
import nl.han.oose.dea.rest.services.dto.Track.TrackDTO;

import java.util.List;


@Path("/tracks")
public class TrackResource {

    private PlaylistDAO playlistDAO = new PlaylistDAO();

    private TrackDAO trackDAO = new TrackDAO();
    private UserDAO userDAO = new UserDAO();

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

