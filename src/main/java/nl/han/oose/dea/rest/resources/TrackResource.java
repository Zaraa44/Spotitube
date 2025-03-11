package nl.han.oose.dea.rest.resources;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import nl.han.oose.dea.rest.services.dto.Track.TrackDTO;
import nl.han.oose.dea.rest.services.dto.Track.TracksResponseDTO;
import nl.han.oose.dea.rest.services.dto.Playlist.PlaylistDTO;

import java.util.ArrayList;
import java.util.List;

@Path("/tracks")
public class TrackResource {

    private static List<TrackDTO> allTracks = new ArrayList<>();

    static {
        // Example tracks initialization
        allTracks.add(new TrackDTO(3, "Ocean and a rock", "Lisa Hannigan", 337, "Sea sew", null, null, null, false));
        allTracks.add(new TrackDTO(4, "So Long, Marianne", "Leonard Cohen", 546, "Songs of Leonard Cohen", null, null, null, false));
        allTracks.add(new TrackDTO(5, "One", "Metallica", 423, null, 37, "18-03-2001", "Long version", true));
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAvailableTracks(@QueryParam("forPlaylist") int forPlaylist,
                                       @QueryParam("token") String token) {

        List<TrackDTO> availableTracks = new ArrayList<>(allTracks);

        PlaylistDTO playlist = PlaylistResource.getPlaylistById(forPlaylist);
        if (playlist != null && playlist.getTracks() != null) {
            availableTracks.removeIf(track ->
                    playlist.getTracks().stream().anyMatch(pt -> pt.getId() == track.getId())
            );
        }

        return Response.ok(new TracksResponseDTO(availableTracks)).build();
    }

    public static List<TrackDTO> getAllTracks() {
        return allTracks;
    }

}
