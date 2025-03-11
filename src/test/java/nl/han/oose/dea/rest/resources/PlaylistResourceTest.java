package nl.han.oose.dea.rest.resources;

import jakarta.ws.rs.core.Response;
import nl.han.oose.dea.rest.resources.PlaylistResource;
import nl.han.oose.dea.rest.services.dto.Playlist.PlaylistDTO;
import nl.han.oose.dea.rest.services.dto.Playlist.PlaylistResponseDTO;
import nl.han.oose.dea.rest.services.dto.Track.TrackDTO;
import nl.han.oose.dea.rest.services.dto.Track.TracksResponseDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;

public class PlaylistResourceTest {

    private PlaylistResource sut;

    @BeforeEach
    void setup() {
        sut = new PlaylistResource();

    }

    @Test
    void addPlaylistIncreasesListSize() {
        // Arrange
        PlaylistDTO newPlaylist = new PlaylistDTO(0, "TestPlaylist", false, Collections.emptyList());

        // Act
        Response response = sut.addPlaylist("TestToken", newPlaylist);

        // Assert
        Assertions.assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        PlaylistResponseDTO responseDTO = (PlaylistResponseDTO) response.getEntity();
        Assertions.assertEquals(1, responseDTO.getPlaylists().size());
    }

    @Test
    void deletePlaylistRemovesPlaylist() {
        // Arrange
        PlaylistDTO playlist = new PlaylistDTO(1, "TestPlaylist", false, Collections.emptyList());
        sut.addPlaylist("TestToken", playlist);

        // Act
        Response deleteResponse = sut.deletePlaylist(1, "TestToken");

        // Assert
        Assertions.assertEquals(Response.Status.OK.getStatusCode(), deleteResponse.getStatus());
        PlaylistResponseDTO responseDTO = (PlaylistResponseDTO) deleteResponse.getEntity();
        Assertions.assertTrue(responseDTO.getPlaylists().isEmpty());
    }

    @Test
    void editPlaylistUpdatesName() {
        // Arrange
        PlaylistDTO playlist = new PlaylistDTO(1, "Old Name", false, Collections.emptyList());
        sut.addPlaylist("valid-token", playlist);

        PlaylistDTO updatedPlaylist = new PlaylistDTO();
        updatedPlaylist.setName("New Name");

        // Act
        Response response = sut.editPlaylist(1, "valid-token", updatedPlaylist);

        // Assert
        Assertions.assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        PlaylistResponseDTO responseDTO = (PlaylistResponseDTO) response.getEntity();
        Assertions.assertEquals("New Name", responseDTO.getPlaylists().get(0).getName());
    }

}
