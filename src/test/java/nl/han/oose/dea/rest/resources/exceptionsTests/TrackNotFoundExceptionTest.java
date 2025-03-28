package nl.han.oose.dea.rest.resources.exceptionsTests;

import nl.han.oose.dea.rest.services.exceptions.TrackNotFoundException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TrackNotFoundExceptionTest {

    @Test
    void exceptionShouldContainCorrectMessage() {
        // Arrange
        String expectedMessage = "Track not found";

        // Act
        TrackNotFoundException exception = assertThrows(
                TrackNotFoundException.class,
                () -> {
                    throw new TrackNotFoundException(expectedMessage);
                }
        );

        // Assert
        assertEquals(expectedMessage, exception.getMessage());
    }
}
