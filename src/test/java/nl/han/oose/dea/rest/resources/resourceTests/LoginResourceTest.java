package nl.han.oose.dea.rest.resources.resourceTests;

import nl.han.oose.dea.rest.datasource.DAO.UserDAO;
import nl.han.oose.dea.rest.datasource.DAO.PlaylistDAO;
import nl.han.oose.dea.rest.resources.LoginResource;
import nl.han.oose.dea.rest.services.dto.Login.LoginRequestDTO;
import nl.han.oose.dea.rest.services.dto.User.UserDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;

import jakarta.ws.rs.core.Response;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.lang.reflect.Field;

class LoginResourceTest {

    private LoginResource sut;
    private UserDAO userDAO;
    private PlaylistDAO playlistDAO;

    @BeforeEach
    void setUp() throws Exception {
        // Arrange
        sut = new LoginResource();
        userDAO = mock(UserDAO.class);
        playlistDAO = mock(PlaylistDAO.class);

        // Inject mocks met reflection
        Field userDaoField = LoginResource.class.getDeclaredField("userDAO");
        userDaoField.setAccessible(true);
        userDaoField.set(sut, userDAO);

        Field playlistDaoField = LoginResource.class.getDeclaredField("playlistDAO");
        playlistDaoField.setAccessible(true);
        playlistDaoField.set(sut, playlistDAO);
    }

    @Test
    void loginShouldReturnOkWithValidCredentials() {
        // Arrange
        String plainPassword = "password123";
        String hashedPassword = BCrypt.hashpw(plainPassword, BCrypt.gensalt());

        var request = new LoginRequestDTO();
        request.setUser("jane");
        request.setPassword(plainPassword);

        when(userDAO.getUserByUsername("jane")).thenReturn(new UserDTO("jane", hashedPassword));

        // Act
        Response response = sut.login(request);

        // Assert
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }

    @Test
    void loginShouldFailWithInvalidPassword() {
        // Arrange
        String wrongPassword = "wrong";
        String correctHashed = BCrypt.hashpw("correct", BCrypt.gensalt());

        var request = new LoginRequestDTO();
        request.setUser("john");
        request.setPassword(wrongPassword);

        when(userDAO.getUserByUsername("john")).thenReturn(new UserDTO("john", correctHashed));

        // Act
        Response response = sut.login(request);

        // Assert
        assertEquals(Response.Status.UNAUTHORIZED.getStatusCode(), response.getStatus());
    }

    @Test
    void loginShouldFailWhenUserNotFound() {
        // Arrange
        var request = new LoginRequestDTO();
        request.setUser("unknown");
        request.setPassword("irrelevant");

        when(userDAO.getUserByUsername("unknown")).thenReturn(null);

        // Act
        Response response = sut.login(request);

        // Assert
        assertEquals(Response.Status.UNAUTHORIZED.getStatusCode(), response.getStatus());
    }
}
