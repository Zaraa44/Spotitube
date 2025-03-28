package nl.han.oose.dea.rest.resources.daoTests;

import nl.han.oose.dea.rest.datasource.mappers.PlaylistDAOMapper;
import nl.han.oose.dea.rest.services.dto.playlist.PlaylistDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.ResultSet;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PlaylistDAOMapperTest {

    private PlaylistDAOMapper sut;
    private ResultSet resultSetMock;

    @BeforeEach
    void setUp() {
        sut = new PlaylistDAOMapper();
        resultSetMock = mock(ResultSet.class);
    }

    @Test
    void mapToPlaylistDTO_ReturnsCorrectDTO() throws Exception {
        // Arrange
        when(resultSetMock.getInt("ID")).thenReturn(1);
        when(resultSetMock.getString("NAME")).thenReturn("My Playlist");
        when(resultSetMock.getString("USERNAME")).thenReturn("nisa");
        when(resultSetMock.getBoolean("OWNER")).thenReturn(true);

        // Act
        PlaylistDTO result = sut.mapToPlaylistDTO(resultSetMock);

        // Assert
        assertEquals(1, result.getId());
        assertEquals("My Playlist", result.getName());
        assertTrue(result.isOwner());
    }
}
