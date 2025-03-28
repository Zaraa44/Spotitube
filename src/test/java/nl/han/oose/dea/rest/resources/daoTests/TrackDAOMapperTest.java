package nl.han.oose.dea.rest.resources.daoTests;

import nl.han.oose.dea.rest.datasource.mappers.TrackDAOMapper;
import nl.han.oose.dea.rest.services.dto.track.TrackDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.ResultSet;
import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TrackDAOMapperTest {

    private TrackDAOMapper sut;
    private ResultSet resultSetMock;

    @BeforeEach
    void setUp() {
        sut = new TrackDAOMapper();
        resultSetMock = mock(ResultSet.class);
    }

    @Test
    void mapToTrackDTO_ReturnsCorrectTrackDTO() throws Exception {
        // Arrange
        when(resultSetMock.getInt("TRACK_ID")).thenReturn(1);
        when(resultSetMock.getString("TITLE")).thenReturn("Test Title");
        when(resultSetMock.getString("PERFORMER")).thenReturn("Test Artist");
        when(resultSetMock.getInt("DURATION")).thenReturn(300);
        when(resultSetMock.getString("ALBUM")).thenReturn("Test Album");
        when(resultSetMock.getObject("PLAYCOUNT")).thenReturn(42);
        when(resultSetMock.getInt("PLAYCOUNT")).thenReturn(42); // belangrijk!
        when(resultSetMock.getTimestamp("PUBLICATIONDATE")).thenReturn(Timestamp.valueOf("2020-03-19 00:00:00"));
        when(resultSetMock.getString("DESCRIPTION")).thenReturn("A test track");
        when(resultSetMock.getBoolean("OFFLINEAVAILABLE")).thenReturn(true);

        // Act
        TrackDTO result = sut.mapToTrackDTO(resultSetMock);

        // Assert
        assertEquals(1, result.getId());
        assertEquals("Test Title", result.getTitle());
        assertEquals("Test Artist", result.getPerformer());
        assertEquals(300, result.getDuration());
        assertEquals("Test Album", result.getAlbum());
        assertEquals(42, result.getPlaycount());
        assertEquals("03-19-2020", result.getPublicationDate());
        assertEquals("A test track", result.getDescription());
        assertTrue(result.isOfflineAvailable());
    }

    @Test
    void mapToTrackDTO_NullableFieldsHandledGracefully() throws Exception {
        // Arrange
        when(resultSetMock.getInt("TRACK_ID")).thenReturn(2);
        when(resultSetMock.getString("TITLE")).thenReturn("No Album");
        when(resultSetMock.getString("PERFORMER")).thenReturn("No Artist");
        when(resultSetMock.getInt("DURATION")).thenReturn(180);
        when(resultSetMock.getString("ALBUM")).thenReturn(null);
        when(resultSetMock.getObject("PLAYCOUNT")).thenReturn(null);
        when(resultSetMock.getTimestamp("PUBLICATIONDATE")).thenReturn(null);
        when(resultSetMock.getString("DESCRIPTION")).thenReturn(null);
        when(resultSetMock.getBoolean("OFFLINEAVAILABLE")).thenReturn(false);

        // Act
        TrackDTO result = sut.mapToTrackDTO(resultSetMock);

        // Assert
        assertEquals(2, result.getId());
        assertEquals("No Album", result.getTitle()); // toegevoegd: bevestigt titel blijft correct
        assertEquals("No Artist", result.getPerformer()); // toegevoegd: performer klopt
        assertEquals(180, result.getDuration()); // toegevoegd: duur klopt
        assertNull(result.getAlbum());
        assertNull(result.getPlaycount());
        assertNull(result.getPublicationDate());
        assertNull(result.getDescription());
        assertFalse(result.isOfflineAvailable());
    }
}
