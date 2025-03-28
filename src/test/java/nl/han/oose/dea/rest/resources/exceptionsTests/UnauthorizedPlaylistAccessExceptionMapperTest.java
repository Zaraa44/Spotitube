package nl.han.oose.dea.rest.resources.exceptionsTests;

import jakarta.ws.rs.core.Response;
import nl.han.oose.dea.rest.services.exceptions.UnauthorizedPlaylistAccessException;
import nl.han.oose.dea.rest.resources.exceptionMappers.UnauthorizedPlaylistAccessExceptionMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UnauthorizedPlaylistAccessExceptionMapperTest {

    @Test
    void toResponse_ReturnsForbiddenResponseWithMessage() {
        // Arrange
        var mapper = new UnauthorizedPlaylistAccessExceptionMapper();
        var exception = new UnauthorizedPlaylistAccessException("Not allowed to access this playlist");

        // Act
        Response response = mapper.toResponse(exception);

        // Assert
        assertEquals(Response.Status.FORBIDDEN.getStatusCode(), response.getStatus());
        assertEquals("Not allowed to access this playlist", response.getEntity());
    }
}
