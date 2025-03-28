package nl.han.oose.dea.rest.resources.resourceTests;

import jakarta.ws.rs.core.Response;
import nl.han.oose.dea.rest.datasource.dao.PlaylistDAO;
import nl.han.oose.dea.rest.datasource.dao.TrackDAO;
import nl.han.oose.dea.rest.datasource.dao.UserDAO;
import nl.han.oose.dea.rest.resources.PlaylistResource;
import nl.han.oose.dea.rest.services.dto.playlist.PlaylistDTO;
import nl.han.oose.dea.rest.services.dto.playlist.PlaylistResponseDTO;
import nl.han.oose.dea.rest.services.dto.track.TrackDTO;
import nl.han.oose.dea.rest.services.dto.track.TracksResponseDTO;
import nl.han.oose.dea.rest.services.exceptions.UnauthorizedPlaylistAccessException;
import nl.han.oose.dea.rest.services.exceptions.InvalidTokenException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PlaylistResourceTest {

    private PlaylistResource sut;
    private UserDAO userDAOMock;
    private PlaylistDAO playlistDAOMock;
    private TrackDAO trackDAOMock;

    @BeforeEach
    void setUp() {

        userDAOMock = mock(UserDAO.class);
        playlistDAOMock = mock(PlaylistDAO.class);
        trackDAOMock = mock(TrackDAO.class);


        sut = new PlaylistResource(playlistDAOMock, userDAOMock, trackDAOMock);
    }


    @Test
    void getPlaylists_ShouldReturnResponse_WhenTokenIsValid() {
        // Arrange
        String token = "valid-token";
        String username = "testuser";
        PlaylistDTO playlist = new PlaylistDTO(1, "Rock", true);
        when(userDAOMock.getUsernameByToken(token)).thenReturn(username);
        when(playlistDAOMock.getAllPlaylists(username)).thenReturn(List.of(playlist));
        when(playlistDAOMock.calculateTotalLength()).thenReturn(1000);

        // Act
        Response response = sut.getPlaylists(token);
        PlaylistResponseDTO entity = (PlaylistResponseDTO) response.getEntity();

        // Assert
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertNotNull(entity);
        assertEquals(1, entity.getPlaylists().size());
        assertEquals(1000, entity.getLength());
    }

    @Test
    void getPlaylists_ShouldThrowInvalidTokenException_WhenTokenMissing() {
        // Arrange
        String token = "";

        // Act + Assert
        assertThrows(InvalidTokenException.class, () -> sut.getPlaylists(token));
    }

    @Test
    void getPlaylists_ShouldThrowInvalidTokenException_WhenTokenInvalid() {
        // Arrange
        String token = "invalid-token";
        when(userDAOMock.getUsernameByToken(token)).thenReturn(null);

        // Act + Assert
        assertThrows(InvalidTokenException.class, () -> sut.getPlaylists(token));
    }


    @Test
    void addPlaylist_ShouldAddPlaylistAndReturnUpdatedResponse_WhenTokenIsValid() {
        // Arrange
        String token = "valid-token";
        String username = "testuser";
        PlaylistDTO newPlaylist = new PlaylistDTO(0, "Chill", true);
        PlaylistDTO expectedPlaylist = new PlaylistDTO(2, "Chill", true);
        when(userDAOMock.getUsernameByToken(token)).thenReturn(username);

        doNothing().when(playlistDAOMock).addPlaylist("Chill", username);
        when(playlistDAOMock.getAllPlaylists(username)).thenReturn(List.of(expectedPlaylist));
        when(playlistDAOMock.calculateTotalLength()).thenReturn(2000);

        // Act
        Response response = sut.addPlaylist(token, newPlaylist);
        PlaylistResponseDTO entity = (PlaylistResponseDTO) response.getEntity();

        // Assert
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertNotNull(entity);
        assertEquals(1, entity.getPlaylists().size());
        assertEquals(2000, entity.getLength());
        verify(playlistDAOMock).addPlaylist("Chill", username);
    }

    @Test
    void updatePlaylistName_ShouldReturnUpdatedResponse_WhenUserIsOwner() {
        // Arrange
        String token = "valid-token";
        String username = "testuser";
        int playlistId = 1;
        PlaylistDTO updatedPlaylist = new PlaylistDTO(1, "New Name", true);
        when(userDAOMock.getUsernameByToken(token)).thenReturn(username);
        when(playlistDAOMock.isOwnerOfPlaylist(token, playlistId)).thenReturn(true);
        doNothing().when(playlistDAOMock).updatePlaylistName(playlistId, "New Name");
        when(playlistDAOMock.getAllPlaylists(username)).thenReturn(List.of(updatedPlaylist));
        when(playlistDAOMock.calculateTotalLength()).thenReturn(1500);

        // Act
        Response response = sut.updatePlaylistName(playlistId, token, updatedPlaylist);
        PlaylistResponseDTO entity = (PlaylistResponseDTO) response.getEntity();

        // Assert
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertNotNull(entity);
        assertEquals("New Name", entity.getPlaylists().get(0).getName());
    }

    @Test
    void updatePlaylistName_ShouldThrowUnauthorizedPlaylistAccessException_WhenUserIsNotOwner() {
        // Arrange
        String token = "valid-token";
        String username = "testuser";
        int playlistId = 1;
        PlaylistDTO updatedPlaylist = new PlaylistDTO(1, "New Name", false);
        when(userDAOMock.getUsernameByToken(token)).thenReturn(username);
        when(playlistDAOMock.isOwnerOfPlaylist(token, playlistId)).thenReturn(false);

        // Act + Assert
        assertThrows(UnauthorizedPlaylistAccessException.class,
                () -> sut.updatePlaylistName(playlistId, token, updatedPlaylist));
    }

    @Test
    void deletePlaylist_ShouldReturnUpdatedResponse_WhenUserIsOwner() {
        // Arrange
        String token = "valid-token";
        String username = "testuser";
        int playlistId = 1;
        when(userDAOMock.getUsernameByToken(token)).thenReturn(username);
        when(playlistDAOMock.isOwnerOfPlaylist(token, playlistId)).thenReturn(true);
        doNothing().when(playlistDAOMock).deletePlaylist(playlistId);
        PlaylistDTO remainingPlaylist = new PlaylistDTO(2, "Other", true);
        when(playlistDAOMock.getAllPlaylists(username)).thenReturn(List.of(remainingPlaylist));
        when(playlistDAOMock.calculateTotalLength()).thenReturn(500);

        // Act
        Response response = sut.deletePlaylist(playlistId, token);
        PlaylistResponseDTO entity = (PlaylistResponseDTO) response.getEntity();

        // Assert
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertNotNull(entity);
        assertEquals(1, entity.getPlaylists().size());
        verify(playlistDAOMock).deletePlaylist(playlistId);
    }

    @Test
    void deletePlaylist_ShouldThrowUnauthorizedPlaylistAccessException_WhenUserIsNotOwner() {
        // Arrange
        String token = "valid-token";
        String username = "testuser";
        int playlistId = 1;
        when(userDAOMock.getUsernameByToken(token)).thenReturn(username);
        when(playlistDAOMock.isOwnerOfPlaylist(token, playlistId)).thenReturn(false);

        // Act + Assert
        assertThrows(UnauthorizedPlaylistAccessException.class,
                () -> sut.deletePlaylist(playlistId, token));
    }


    @Test
    void getTracksForPlaylist_ShouldReturnTracksResponse_WhenTokenIsValid() {
        // Arrange
        String token = "valid-token";
        String username = "testuser";
        int playlistId = 1;
        TrackDTO track = new TrackDTO(4, "Song", "Artist", 300, "Album", null, null, null, false);
        when(userDAOMock.getUsernameByToken(token)).thenReturn(username);
        when(trackDAOMock.getTracksForPlaylist(playlistId)).thenReturn(List.of(track));

        // Act
        Response response = sut.getTracksForPlaylist(playlistId, token);
        TracksResponseDTO entity = (TracksResponseDTO) response.getEntity();

        // Assert
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertNotNull(entity);
        assertEquals(1, entity.getTracks().size());
    }

    @Test
    void addTrackToPlaylist_ShouldReturnUpdatedTracksResponse_WhenTokenIsValid() {
        // Arrange
        String token = "valid-token";
        String username = "testuser";
        int playlistId = 1;
        TrackDTO trackDTO = new TrackDTO(4, "Song", "Artist", 300, "Album", null, null, null, false);
        when(userDAOMock.getUsernameByToken(token)).thenReturn(username);
        doNothing().when(trackDAOMock).addTrackToPlaylist(playlistId, trackDTO.getId(), trackDTO.isOfflineAvailable());
        when(trackDAOMock.getTracksForPlaylist(playlistId)).thenReturn(List.of(trackDTO));

        // Act
        Response response = sut.addTrackToPlaylist(playlistId, token, trackDTO);
        TracksResponseDTO entity = (TracksResponseDTO) response.getEntity();

        // Assert
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertNotNull(entity);
        assertEquals(1, entity.getTracks().size());
        verify(trackDAOMock).addTrackToPlaylist(playlistId, trackDTO.getId(), trackDTO.isOfflineAvailable());
    }

    @Test
    void removeTrackFromPlaylist_ShouldReturnUpdatedTracksResponse_WhenTokenIsValidAndUserIsOwner() {
        // Arrange
        String token = "valid-token";
        String username = "testuser";
        int playlistId = 1;
        int trackId = 4;
        when(userDAOMock.getUsernameByToken(token)).thenReturn(username);
        when(playlistDAOMock.isOwnerOfPlaylist(token, playlistId)).thenReturn(true);
        doNothing().when(trackDAOMock).removeTrackFromPlaylist(playlistId, trackId);

        when(trackDAOMock.getTracksForPlaylist(playlistId)).thenReturn(List.of());

        // Act
        Response response = sut.removeTrackFromPlaylist(playlistId, trackId, token);
        TracksResponseDTO entity = (TracksResponseDTO) response.getEntity();

        // Assert
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertNotNull(entity);
        assertEquals(0, entity.getTracks().size());
        verify(trackDAOMock).removeTrackFromPlaylist(playlistId, trackId);
    }

    @Test
    void removeTrackFromPlaylist_ShouldThrowUnauthorizedPlaylistAccessException_WhenUserIsNotOwner() {
        // Arrange
        String token = "valid-token";
        String username = "testuser";
        int playlistId = 1;
        int trackId = 4;
        when(userDAOMock.getUsernameByToken(token)).thenReturn(username);
        when(playlistDAOMock.isOwnerOfPlaylist(token, playlistId)).thenReturn(false);

        // Act + Assert
        assertThrows(UnauthorizedPlaylistAccessException.class,
                () -> sut.removeTrackFromPlaylist(playlistId, trackId, token));
    }
}
