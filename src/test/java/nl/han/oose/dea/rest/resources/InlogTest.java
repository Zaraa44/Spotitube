package nl.han.oose.dea.rest.resources;

import jakarta.ws.rs.core.Response;
import nl.han.oose.dea.rest.services.dto.Login.LoginRequestDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class InlogTest {

    private InlogResource sut;

    @BeforeEach
    void setup() {
        sut = new InlogResource();
    }

    @Test
    void loginFailsForInvalidCredentials() {
        // Arrange
        LoginRequestDTO loginRequest = new LoginRequestDTO();
        loginRequest.setUser("FoutUsername");
        loginRequest.setPassword("FoutPassword");

        // Act
        Response loginResponse = sut.login(loginRequest);

        // Assert
        Assertions.assertEquals(Response.Status.UNAUTHORIZED.getStatusCode(), loginResponse.getStatus());
    }
}
