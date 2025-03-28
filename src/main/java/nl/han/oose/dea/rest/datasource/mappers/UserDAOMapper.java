package nl.han.oose.dea.rest.datasource.mappers;

import jakarta.enterprise.context.ApplicationScoped;
import nl.han.oose.dea.rest.services.dto.user.UserDTO;

import java.sql.ResultSet;
import java.sql.SQLException;

@ApplicationScoped
public class UserDAOMapper {
    public UserDTO mapToUserDTO(ResultSet rs) throws SQLException {
        return new UserDTO(
                rs.getString("USERNAME"),
                rs.getString("PASSWORD")
        );
    }
}
