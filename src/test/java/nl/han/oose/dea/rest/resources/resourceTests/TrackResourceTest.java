package nl.han.oose.dea.rest.resources.resourceTests;

import jakarta.ws.rs.core.Response;
import nl.han.oose.dea.rest.datasource.DAO.TrackDAO;
import nl.han.oose.dea.rest.datasource.DAO.UserDAO;
import nl.han.oose.dea.rest.resources.TrackResource;
import nl.han.oose.dea.rest.services.dto.Track.TrackDTO;
import nl.han.oose.dea.rest.services.dto.Track.TracksResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TrackResourceTest {

    private TrackDAO trackDAOMock;
    private UserDAO userDAOMock;
    private TrackResource sut;

    @BeforeEach
    void setUp() {
        trackDAOMock = mock(TrackDAO.class);
        userDAOMock = mock(UserDAO.class);
        sut = new TrackResource(trackDAOMock, userDAOMock); // constructor-injectie
    }

    @Test
    void getAvailableTracksReturnsTracksWhenTokenIsValid() {
        // Arrange
        String token = "valid-token";
        int playlistId = 1;
        List<TrackDTO> dummyTracks = List.of(new TrackDTO(1, "Title", "Performer", 300, null, null, null, null, false));
        when(userDAOMock.getUsernameByToken(token)).thenReturn("user1");
        when(trackDAOMock.getTracksNotInPlaylist(playlistId)).thenReturn(dummyTracks);

        // Act
        Response response = sut.getAvailableTracks(playlistId, token);
        TracksResponseDTO entity = (TracksResponseDTO) response.getEntity();

        // Assert
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertNotNull(entity);
        assertEquals(1, entity.getTracks().size());
        assertEquals("Title", entity.getTracks().get(0).getTitle());
    }

    @Test
    void getAvailableTracksReturnsUnauthorizedWhenTokenInvalid() {
        // Arrange
        String invalidToken = "invalid-token";
        when(userDAOMock.getUsernameByToken(invalidToken)).thenReturn(null);

        // Act
        Response response = sut.getAvailableTracks(1, invalidToken);

        // Assert
        assertEquals(Response.Status.UNAUTHORIZED.getStatusCode(), response.getStatus());
        assertEquals("Invalid token", response.getEntity());
    }
}
