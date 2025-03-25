package nl.han.oose.dea.rest.resources;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import nl.han.oose.dea.rest.datasource.data.PlaylistDAO;
import nl.han.oose.dea.rest.datasource.data.UserDAO;
import nl.han.oose.dea.rest.datasource.data.TrackDAO;
import nl.han.oose.dea.rest.services.dto.Playlist.PlaylistDTO;
import nl.han.oose.dea.rest.services.dto.Playlist.PlaylistResponseDTO;
import nl.han.oose.dea.rest.services.dto.Track.TrackDTO;
import nl.han.oose.dea.rest.services.dto.Track.TracksResponseDTO;
import nl.han.oose.dea.rest.services.exceptions.Playlist.UnauthorizedPlaylistAccessException;
import nl.han.oose.dea.rest.services.exceptions.Token.InvalidTokenException;

import java.util.List;

@Path("/playlists")
public class PlaylistResource {

    private PlaylistDAO playlistDAO = new PlaylistDAO();
    private UserDAO userDAO = new UserDAO();
    private TrackDAO trackDAO = new TrackDAO();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPlaylists(@QueryParam("token") String token) {
        if (token == null || token.isEmpty()) {
            throw new InvalidTokenException("Token is missing");
        }

        String username = userDAO.getUsernameByToken(token);

        if (username == null) {
            throw new InvalidTokenException("Invalid token");
        }

        List<PlaylistDTO> playlists = playlistDAO.getAllPlaylists(username);
        int totalLength = playlistDAO.calculateTotalLength();
        return Response.ok(new PlaylistResponseDTO(playlists, totalLength)).build();
    }


    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addPlaylist(@QueryParam("token") String token, PlaylistDTO newPlaylist) {
        if (token == null || token.isEmpty()) {
            throw new InvalidTokenException("Token is missing");
        }

        String username = userDAO.getUsernameByToken(token);

        if (username == null) {
            throw new InvalidTokenException("Invalid token");
        }

        playlistDAO.addPlaylist(newPlaylist.getName(), username);

        List<PlaylistDTO> playlists = playlistDAO.getAllPlaylists(username);
        int totalLength = playlistDAO.calculateTotalLength();
        return Response.ok(new PlaylistResponseDTO(playlists, totalLength)).build();
    }


    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updatePlaylistName(@PathParam("id") int id,
                                       @QueryParam("token") String token,
                                       PlaylistDTO updatedPlaylist) {
        if (token == null || token.isEmpty()) {
            throw new InvalidTokenException("Token is missing");
        }

        String username = userDAO.getUsernameByToken(token);

        if (username == null) {
            throw new InvalidTokenException("Invalid token");
        }

        if (!playlistDAO.isOwnerOfPlaylist(token, id)) {
            throw new UnauthorizedPlaylistAccessException("Not allowed to modify this playlist");
        }

        playlistDAO.updatePlaylistName(id, updatedPlaylist.getName());

        List<PlaylistDTO> playlists = playlistDAO.getAllPlaylists(username);
        int totalLength = playlistDAO.calculateTotalLength();
        return Response.ok(new PlaylistResponseDTO(playlists, totalLength)).build();
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deletePlaylist(@PathParam("id") int id,
                                   @QueryParam("token") String token) {
        if (token == null || token.isEmpty()) {
            throw new InvalidTokenException("Token is missing");
        }

        String username = userDAO.getUsernameByToken(token);

        if (username == null) {
            throw new InvalidTokenException("Invalid token");
        }

        if (!playlistDAO.isOwnerOfPlaylist(token, id)) {
            throw new UnauthorizedPlaylistAccessException("Not allowed to delete this playlist");
        }

        playlistDAO.deletePlaylist(id);

        List<PlaylistDTO> playlists = playlistDAO.getAllPlaylists(username);
        int totalLength = playlistDAO.calculateTotalLength();
        return Response.ok(new PlaylistResponseDTO(playlists, totalLength)).build();
    }


    @GET
    @Path("/{id}/tracks")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTracksForPlaylist(@PathParam("id") int playlistId,
                                         @QueryParam("token") String token) {
        if (token == null || token.isEmpty()) {
            throw new InvalidTokenException("Token is missing");
        }

        String username = userDAO.getUsernameByToken(token);
        if (username == null) {
            throw new InvalidTokenException("Invalid token");
        }

        List<TrackDTO> tracks = trackDAO.getTracksForPlaylist(playlistId);
        return Response.ok(new TracksResponseDTO(tracks)).build();
    }


    @POST
    @Path("/{id}/tracks")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addTrackToPlaylist(@PathParam("id") int playlistId,
                                       @QueryParam("token") String token,
                                       TrackDTO trackDTO) {
        if (token == null || token.isEmpty()) {
            throw new InvalidTokenException("Token is missing");
        }

        String username = userDAO.getUsernameByToken(token);
        if (username == null) {
            throw new InvalidTokenException("Invalid token");
        }

        trackDAO.addTrackToPlaylist(playlistId, trackDTO.getId(), trackDTO.isOfflineAvailable());

        List<TrackDTO> tracks = trackDAO.getTracksForPlaylist(playlistId);
        return Response.ok(new TracksResponseDTO(tracks)).build();
    }


    @DELETE
    @Path("/{playlistId}/tracks/{trackId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response removeTrackFromPlaylist(@PathParam("playlistId") int playlistId,
                                            @PathParam("trackId") int trackId,
                                            @QueryParam("token") String token) {
        if (token == null || token.isEmpty()) {
            throw new InvalidTokenException("Token is missing");
        }

        String username = userDAO.getUsernameByToken(token);
        if (username == null) {
            throw new InvalidTokenException("Invalid token");
        }

        if (!playlistDAO.isOwnerOfPlaylist(token, playlistId)) {
            throw new UnauthorizedPlaylistAccessException("Not allowed to modify this playlist");
        }

        trackDAO.removeTrackFromPlaylist(playlistId, trackId);

        List<TrackDTO> updatedTracks = trackDAO.getTracksForPlaylist(playlistId);
        return Response.ok(new TracksResponseDTO(updatedTracks)).build();
    }

}
