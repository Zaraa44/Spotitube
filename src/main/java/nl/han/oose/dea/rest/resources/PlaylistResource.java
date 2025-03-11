package nl.han.oose.dea.rest.resources;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import nl.han.oose.dea.rest.services.dto.Playlist.PlaylistDTO;
import nl.han.oose.dea.rest.services.dto.Playlist.PlaylistResponseDTO;
import nl.han.oose.dea.rest.services.dto.Track.TrackDTO;
import nl.han.oose.dea.rest.services.dto.Track.TracksResponseDTO;

import java.util.ArrayList;
import java.util.List;

@Path("/playlists")
public class PlaylistResource {

    private static List<PlaylistDTO> playlists = new ArrayList<>();
    private static int totalLength = 120000;
    private static int nextId = 3;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllPlaylists(@QueryParam("token") String token) {
        return Response.ok(new PlaylistResponseDTO(playlists, totalLength)).build();
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deletePlaylist(@PathParam("id") int id, @QueryParam("token") String token) {
        playlists.removeIf(playlist -> playlist.getId() == id);
        totalLength -= 59000;
        return Response.ok(new PlaylistResponseDTO(playlists, totalLength)).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addPlaylist(@QueryParam("token") String token, PlaylistDTO newPlaylist) {
        newPlaylist.setId(nextId++);
        newPlaylist.setOwner(true);
        if(newPlaylist.getTracks() == null) {
            newPlaylist.setTracks(new ArrayList<>());
        }
        playlists.add(newPlaylist);
        return Response.ok(new PlaylistResponseDTO(playlists, totalLength)).build();
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response editPlaylist(@PathParam("id") int id, @QueryParam("token") String token, PlaylistDTO updatedPlaylist) {
        for (PlaylistDTO playlist : playlists) {
            if (playlist.getId() == id) {
                playlist.setName(updatedPlaylist.getName());
                break;
            }
        }
        return Response.ok(new PlaylistResponseDTO(playlists, totalLength)).build();
    }

    @GET
    @Path("/{id}/tracks")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTracksForPlaylist(@PathParam("id") int playlistId, @QueryParam("token") String token) {
        PlaylistDTO playlist = getPlaylistById(playlistId);
        if (playlist == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        List<TrackDTO> tracks = playlist.getTracks();
        if (tracks == null) {
            tracks = new ArrayList<>();
            playlist.setTracks(tracks);
        }
        return Response.ok(new TracksResponseDTO(tracks)).build();
    }

    @POST
    @Path("/{id}/tracks")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addTrackToPlaylist(@PathParam("id") int playlistId, @QueryParam("token") String token, TrackDTO trackFromRequest) {
        PlaylistDTO playlist = getPlaylistById(playlistId);
        if (playlist == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        List<TrackDTO> tracks = playlist.getTracks();
        if (tracks == null) {
            tracks = new ArrayList<>();
            playlist.setTracks(tracks);
        }

        TrackDTO completeTrack = findCompleteTrack(trackFromRequest.getId());
        if (completeTrack == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Track not found").build();
        }

        completeTrack.setOfflineAvailable(trackFromRequest.isOfflineAvailable());

        tracks.add(completeTrack);
        return Response.ok(new TracksResponseDTO(tracks)).build();
    }

    private TrackDTO findCompleteTrack(int trackId) {
        for (TrackDTO available : TrackResource.getAllTracks()) {
            if (available.getId() == trackId) {
                return available;
            }
        }
        return null;
    }


    @DELETE
    @Path("/{id}/tracks/{trackId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response removeTrackFromPlaylist(@PathParam("id") int playlistId,
                                            @PathParam("trackId") int trackId,
                                            @QueryParam("token") String token) {
        PlaylistDTO playlist = getPlaylistById(playlistId);
        if (playlist == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        List<TrackDTO> tracks = playlist.getTracks();
        if (tracks != null) {
            tracks.removeIf(track -> track.getId() == trackId);
        }
        return Response.ok(new TracksResponseDTO(tracks)).build();
    }

    public static PlaylistDTO getPlaylistById(int id) {
        for (PlaylistDTO playlist : playlists) {
            if (playlist.getId() == id) {
                return playlist;
            }
        }
        return null;
    }
}
