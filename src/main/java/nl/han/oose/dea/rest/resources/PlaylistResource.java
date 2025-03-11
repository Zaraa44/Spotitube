package nl.han.oose.dea.rest.resources;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import nl.han.oose.dea.rest.services.dto.Playlist.PlaylistDTO;
import nl.han.oose.dea.rest.services.dto.Playlist.PlaylistResponseDTO;

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
}
