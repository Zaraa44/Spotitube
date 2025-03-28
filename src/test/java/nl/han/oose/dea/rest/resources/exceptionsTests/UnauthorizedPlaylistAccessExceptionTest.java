package nl.han.oose.dea.rest.resources.exceptionsTests;

import nl.han.oose.dea.rest.services.exceptions.UnauthorizedPlaylistAccessException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UnauthorizedPlaylistAccessExceptionTest {

    @Test
    void testExceptionMessageIsCorrect() {
        // Arrange
        String expectedMessage = "Not allowed to modify this playlist";

        // Act
        UnauthorizedPlaylistAccessException exception =
                assertThrows(UnauthorizedPlaylistAccessException.class, () -> {
                    throw new UnauthorizedPlaylistAccessException(expectedMessage);
                });

        // Assert
        assertEquals(expectedMessage, exception.getMessage());
    }
}