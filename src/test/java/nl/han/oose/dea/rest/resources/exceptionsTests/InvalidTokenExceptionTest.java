package nl.han.oose.dea.rest.resources.exceptionsTests;
import nl.han.oose.dea.rest.services.exceptions.Token.InvalidTokenException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class InvalidTokenExceptionTest {

    @Test
    void throwsExceptionWithCorrectMessage() {
        // Arrange
        String expectedMessage = "Token is ongeldig";

        // Act
        InvalidTokenException exception = assertThrows(
                InvalidTokenException.class,
                () -> { throw new InvalidTokenException(expectedMessage); }
        );

        // Assert
        assertEquals(expectedMessage, exception.getMessage());
    }
}