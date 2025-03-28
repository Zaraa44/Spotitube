package nl.han.oose.dea.rest.resources.exceptionsTests;

import nl.han.oose.dea.rest.services.exceptions.UserDataAccessException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserDataAccessExceptionTest {

    @Test
    void constructorShouldStoreMessage() {
        // Arrange
        String message = "Fout bij ophalen gebruiker";

        // Act
        UserDataAccessException exception = new UserDataAccessException(message);

        // Assert
        assertEquals(message, exception.getMessage());
    }

    @Test
    void constructorShouldStoreMessageAndCause() {
        // Arrange
        String message = "Databasefout";
        Throwable cause = new RuntimeException("Verbinding verbroken");

        // Act
        UserDataAccessException exception = new UserDataAccessException(message, cause);

        // Assert
        assertEquals(message, exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

    @Test
    void shouldBeInstanceOfRuntimeException() {
        assertTrue(new UserDataAccessException("msg") instanceof RuntimeException);
    }

    @Test
    void shouldBeThrown() {
        assertThrows(UserDataAccessException.class, () -> {
            throw new UserDataAccessException("Kon token niet bijwerken");
        });
    }
}
