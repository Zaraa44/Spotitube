package nl.han.oose.dea.rest.resources.exceptionsTests;

import jakarta.ws.rs.core.Response;
import nl.han.oose.dea.rest.services.exceptions.Track.TrackNotFoundException;
import nl.han.oose.dea.rest.services.exceptions.Track.TrackNotFoundExceptionMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TrackNotFoundExceptionMapperTest {

    @Test
    void toResponse_ReturnsNotFoundStatusAndMessage() {
        // Arrange
        var exceptionMapper = new TrackNotFoundExceptionMapper();
        var exception = new TrackNotFoundException("Track kon niet worden gevonden");

        // Act
        Response response = exceptionMapper.toResponse(exception);

        // Assert
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
        assertEquals("Track kon niet worden gevonden", response.getEntity());
    }
}
