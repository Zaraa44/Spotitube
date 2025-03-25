package nl.han.oose.dea.rest.resources;

import jakarta.ws.rs.core.Response;
import nl.han.oose.dea.rest.datasource.data.UserDAO;
import nl.han.oose.dea.rest.services.dto.User.UserDTO;
import nl.han.oose.dea.rest.services.dto.Login.LoginRequestDTO;
import nl.han.oose.dea.rest.services.dto.Login.LoginResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LoginResourceTest {

    @InjectMocks
    private LoginResource sut;

    @Mock
    private UserDAO userDAO;

    @BeforeEach
    void setup() {
        sut = new LoginResource();

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
        assertEquals(Response.Status.UNAUTHORIZED.getStatusCode(), loginResponse.getStatus());
        assertEquals("Invalid credentials", loginResponse.getEntity());
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
        assertEquals(Response.Status.UNAUTHORIZED.getStatusCode(), loginResponse.getStatus());
        assertEquals("Invalid credentials", loginResponse.getEntity());
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
        assertEquals(Response.Status.UNAUTHORIZED.getStatusCode(), loginResponse.getStatus());
        assertEquals("Invalid credentials", loginResponse.getEntity());
    }
}
