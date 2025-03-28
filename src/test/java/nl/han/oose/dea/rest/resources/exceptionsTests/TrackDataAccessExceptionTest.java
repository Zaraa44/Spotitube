package nl.han.oose.dea.rest.resources.exceptionsTests;

import nl.han.oose.dea.rest.services.exceptions.TrackDataAccessException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TrackDataAccessExceptionTest {

    @Test
    void constructorShouldStoreMessage() {
        // Arrange
        String expectedMessage = "Fout bij ophalen van tracks";

        // Act
        TrackDataAccessException exception = new TrackDataAccessException(expectedMessage);

        // Assert
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    void constructorShouldStoreMessageAndCause() {
        // Arrange
        String expectedMessage = "Database failure";
        Throwable cause = new RuntimeException("Connection timeout");

        // Act
        TrackDataAccessException exception = new TrackDataAccessException(expectedMessage, cause);

        // Assert
        assertEquals(expectedMessage, exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

    @Test
    void shouldBeInstanceOfRuntimeException() {
        assertTrue(new TrackDataAccessException("msg") instanceof RuntimeException);
    }

    @Test
    void shouldBeThrown() {
        assertThrows(TrackDataAccessException.class, () -> {
            throw new TrackDataAccessException("Error occurred");
        });
    }
}
