package nl.han.oose.dea.rest.resources.daoTests;

import nl.han.oose.dea.rest.datasource.mappers.UserDAOMapper;
import nl.han.oose.dea.rest.services.dto.User.UserDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.ResultSet;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserDAOMapperTest {

    private UserDAOMapper sut;
    private ResultSet resultSetMock;

    @BeforeEach
    void setUp() {
        sut = new UserDAOMapper();
        resultSetMock = mock(ResultSet.class);
    }

    @Test
    void mapToUserDTO_ReturnsCorrectDTO() throws Exception {
        // Arrange
        when(resultSetMock.getString("USERNAME")).thenReturn("jane");
        when(resultSetMock.getString("PASSWORD")).thenReturn("secret123");

        // Act
        UserDTO result = sut.mapToUserDTO(resultSetMock);

        // Assert
        assertEquals("jane", result.getUser());
        assertEquals("secret123", result.getPassword());
    }
}
