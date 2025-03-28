package nl.han.oose.dea.rest.resources.exceptionsTests;

import nl.han.oose.dea.rest.services.exceptions.PlaylistDataAccessException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlaylistDataAccessExceptionTest {

    @Test
    void constructorShouldStoreMessage() {
        // Arrange
        String message = "Fout bij playlist";

        // Act
        PlaylistDataAccessException exception = new PlaylistDataAccessException(message);

        // Assert
        assertEquals(message, exception.getMessage());
    }

    @Test
    void constructorShouldStoreMessageAndCause() {
        // Arrange
        String message = "Databasefout";
        Throwable cause = new RuntimeException("Connection error");

        // Act
        PlaylistDataAccessException exception = new PlaylistDataAccessException(message, cause);

        // Assert
        assertEquals(message, exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

    @Test
    void shouldBeInstanceOfRuntimeException() {
        assertTrue(new PlaylistDataAccessException("msg") instanceof RuntimeException);
    }

    @Test
    void shouldBeThrown() {
        assertThrows(PlaylistDataAccessException.class, () -> {
            throw new PlaylistDataAccessException("Fout bij verwijderen");
        });
    }
}
