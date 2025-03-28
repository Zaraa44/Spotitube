package nl.han.oose.dea.rest.resources.exceptionsTests;

import jakarta.ws.rs.core.Response;
import nl.han.oose.dea.rest.services.exceptions.InvalidTokenException;
import nl.han.oose.dea.rest.resources.exceptionMappers.InvalidTokenExceptionMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InvalidTokenExceptionMapperTest {

    @Test
    void toResponse_ReturnsUnauthorizedStatusAndMessage() {
        // Arrange
        var exceptionMapper = new InvalidTokenExceptionMapper();
        var exception = new InvalidTokenException("Token is ongeldig");

        // Act
        Response response = exceptionMapper.toResponse(exception);

        // Assert
        assertEquals(Response.Status.UNAUTHORIZED.getStatusCode(), response.getStatus());
        assertEquals("Token is ongeldig", response.getEntity());
    }
}
