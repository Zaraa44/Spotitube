package nl.han.oose.dea.rest.resources.dtoTests;

import nl.han.oose.dea.rest.services.dto.Playlist.PlaylistRequestDTO;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PlaylistRequestDTOTest {

    @Test
    void setterAndGetterWorkCorrectly() {
        // Arrange
        PlaylistRequestDTO dto = new PlaylistRequestDTO();
        String expectedName = "Chill Vibes";

        // Act
        dto.setName(expectedName);
        String actualName = dto.getName();

        // Assert
        assertEquals(expectedName, actualName);
    }

    @Test
    void defaultConstructorCreatesEmptyName() {
        // Arrange & Act
        PlaylistRequestDTO dto = new PlaylistRequestDTO();

        // Assert
        assertNull(dto.getName());
    }
}
