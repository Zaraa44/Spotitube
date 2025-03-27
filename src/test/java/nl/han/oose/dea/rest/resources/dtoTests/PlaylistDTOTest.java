package nl.han.oose.dea.rest.resources.dtoTests;

import nl.han.oose.dea.rest.services.dto.Playlist.PlaylistDTO;
import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PlaylistDTOTest {

    @Test
    void constructorAndGettersReturnCorrectValues() {
        // Arrange
        int expectedId = 42;
        String expectedName = "My Playlist";
        boolean expectedOwner = true;

        // Act
        PlaylistDTO dto = new PlaylistDTO(expectedId, expectedName, expectedOwner);

        // Assert
        assertEquals(expectedId, dto.getId());
        assertEquals(expectedName, dto.getName());
        assertEquals(expectedOwner, dto.isOwner());
        assertNotNull(dto.getTracks());
        assertTrue(dto.getTracks().isEmpty());
    }

    @Test
    void settersUpdateValuesCorrectly() {
        // Arrange
        PlaylistDTO dto = new PlaylistDTO();
        int newId = 10;
        String newName = "Updated Playlist";
        boolean newOwner = false;
        List<Object> newTracks = List.of("track1", "track2");

        // Act
        dto.setId(newId);
        dto.setName(newName);
        dto.setOwner(newOwner);
        dto.setTracks(newTracks);

        // Assert
        assertEquals(newId, dto.getId());
        assertEquals(newName, dto.getName());
        assertFalse(dto.isOwner());
        assertEquals(newTracks, dto.getTracks());
    }
}
