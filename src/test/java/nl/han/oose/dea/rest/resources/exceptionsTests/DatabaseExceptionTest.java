package nl.han.oose.dea.rest.resources.exceptionsTests;

import nl.han.oose.dea.rest.services.exceptions.DatabaseException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DatabaseExceptionTest {

    @Test
    void constructorShouldSetCorrectMessage() {
        // Arrange & Act
        DatabaseException exception = new DatabaseException();

        // Assert
        assertEquals("Kon database.properties niet laden", exception.getMessage());
    }

    @Test
    void shouldBeInstanceOfRuntimeException() {
        // Arrange & Act
        DatabaseException exception = new DatabaseException();

        // Assert
        assertTrue(exception instanceof RuntimeException);
    }

    @Test
    void shouldBeThrown() {
        // Assert
        assertThrows(DatabaseException.class, () -> {
            // Act
            throw new DatabaseException();
        });
    }
}