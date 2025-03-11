package nl.han.oose.dea.rest.resources;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import nl.han.oose.dea.rest.services.dto.Track.TrackDTO;
import nl.han.oose.dea.rest.services.dto.Track.TracksResponseDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Path("/")
public class TrackResource {

    private static List<TrackDTO> allTracks = new ArrayList<>();

    static {
        // Predefined tracks
        allTracks.add(new TrackDTO(1, "Song for someone", "The Frames", 350, "The cost", null, null, null, false));
        allTracks.add(new TrackDTO(2, "The cost", "The Frames", 423, null, 37, "19-03-2006", "Title song from the Album The Cost", true));
        allTracks.add(new TrackDTO(3, "Ocean and a rock", "Lisa Hannigan", 337, "Sea sew", null, null, null, false));
        allTracks.add(new TrackDTO(4, "So Long, Marianne", "Leonard Cohen", 546, "Songs of Leonard Cohen", null, null, null, false));
        allTracks.add(new TrackDTO(5, "One", "Metallica", 423, null, 37, "18-03-2001", "Long version", true));
    }

    private static List<Integer> playlistTracks = new ArrayList<>(); // Simulated stored track IDs per playlist

    @GET
    @Path("/playlists/{id}/tracks")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTracksForPlaylist(@PathParam("id") int playlistId, @QueryParam("token") String token) {
        List<TrackDTO> tracksInPlaylist = allTracks.stream()
                .filter(track -> playlistTracks.contains(track.getId()))
                .collect(Collectors.toList());

        return Response.ok(new TracksResponseDTO(tracksInPlaylist)).build();
    }

    @GET
    @Path("/tracks")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAvailableTracks(@QueryParam("forPlaylist") Integer playlistId, @QueryParam("token") String token) {
        List<TrackDTO> availableTracks = allTracks.stream()
                .filter(track -> !playlistTracks.contains(track.getId()))
                .collect(Collectors.toList());

        return Response.ok(new TracksResponseDTO(availableTracks)).build();
    }
}
