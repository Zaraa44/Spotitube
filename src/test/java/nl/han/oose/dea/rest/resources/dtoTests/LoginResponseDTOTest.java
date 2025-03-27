package nl.han.oose.dea.rest.resources.dtoTests;

import nl.han.oose.dea.rest.services.dto.Login.LoginResponseDTO;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class LoginResponseDTOTest {

    @Test
    void constructorAndGettersReturnCorrectValues() {
        // Arrange
        String expectedToken = "abc123";
        String expectedUser = "testuser";

        // Act
        LoginResponseDTO dto = new LoginResponseDTO(expectedToken, expectedUser);

        // Assert
        assertEquals(expectedToken, dto.getToken());
        assertEquals(expectedUser, dto.getUser());
    }
}
