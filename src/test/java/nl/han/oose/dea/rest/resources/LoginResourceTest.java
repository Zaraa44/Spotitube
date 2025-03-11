package nl.han.oose.dea.rest.resources;

import jakarta.ws.rs.core.Response;
import nl.han.oose.dea.rest.services.dto.Login.LoginRequestDTO;
import nl.han.oose.dea.rest.services.dto.Login.LoginResponseDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class LoginResourceTest {

    private LoginResource sut;

    @BeforeEach
    void setup() {
        sut = new LoginResource();
    }

    @Test
    void loginFailsForInvalidCredentials() {
        // Arrange
        LoginRequestDTO loginRequest = new LoginRequestDTO();
        loginRequest.setUser("TestFouteUsername");
        loginRequest.setPassword("TestFoutePassword");

        // Act
        Response loginResponse = sut.login(loginRequest);

        // Assert
        Assertions.assertEquals(Response.Status.UNAUTHORIZED.getStatusCode(), loginResponse.getStatus());
    }


    @Test
    void loginSucceedsForValidCredentials() {
        // Arrange
        LoginRequestDTO loginRequest = new LoginRequestDTO();
        loginRequest.setUser("Pasa");
        loginRequest.setPassword("Password123");

        // Act
        Response loginResponse = sut.login(loginRequest);

        // Assert
        Assertions.assertEquals(Response.Status.OK.getStatusCode(), loginResponse.getStatus());

        LoginResponseDTO responseDTO = (LoginResponseDTO) loginResponse.getEntity();
        Assertions.assertNotNull(responseDTO);
        Assertions.assertEquals("1234-1234-1234", responseDTO.getToken());
        Assertions.assertEquals("Pasa", responseDTO.getUser());
    }

    @Test
    void loginFailsForNullUsername() {
        // Arrange
        LoginRequestDTO loginRequest = new LoginRequestDTO();
        loginRequest.setUser(null);
        loginRequest.setPassword("TestPassword");

        // Act
        Response loginResponse = sut.login(loginRequest);

        // Assert
        Assertions.assertEquals(Response.Status.UNAUTHORIZED.getStatusCode(), loginResponse.getStatus());
    }

    @Test
    void loginFailsForNullPassword() {
        // Arrange
        LoginRequestDTO loginRequest = new LoginRequestDTO();
        loginRequest.setUser("TestUsername");
        loginRequest.setPassword(null);

        // Act
        Response loginResponse = sut.login(loginRequest);

        // Assert
        Assertions.assertEquals(Response.Status.UNAUTHORIZED.getStatusCode(), loginResponse.getStatus());
    }

    @Test
    void loginFailsForEmptyUsernameAndPassword() {
        // Arrange
        LoginRequestDTO loginRequest = new LoginRequestDTO();
        loginRequest.setUser("");
        loginRequest.setPassword("");

        // Act
        Response loginResponse = sut.login(loginRequest);

        // Assert
        Assertions.assertEquals(Response.Status.UNAUTHORIZED.getStatusCode(), loginResponse.getStatus());
    }

}
