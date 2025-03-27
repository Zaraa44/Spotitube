package nl.han.oose.dea.rest.datasource.mappers;

import nl.han.oose.dea.rest.services.dto.User.UserDTO;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAOMapper {
    public UserDTO mapToUserDTO(ResultSet rs) throws SQLException {
        return new UserDTO(
                rs.getString("USERNAME"),
                rs.getString("PASSWORD")
        );
    }
}
