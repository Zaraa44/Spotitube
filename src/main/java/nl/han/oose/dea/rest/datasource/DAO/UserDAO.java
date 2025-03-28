package nl.han.oose.dea.rest.datasource.dao;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import nl.han.oose.dea.rest.datasource.mappers.UserDAOMapper;
import nl.han.oose.dea.rest.datasource.util.DatabaseProperties;
import nl.han.oose.dea.rest.services.dto.user.UserDTO;
import nl.han.oose.dea.rest.services.exceptions.UserDataAccessException;

import java.sql.*;

@ApplicationScoped
public class UserDAO {

    private UserDAOMapper mapper;
    private DatabaseProperties dbProps;

    public UserDAO() {
    }

    @Inject
    public void setMapper(UserDAOMapper mapper) {
        this.mapper = mapper;
    }

    @Inject
    public void setDatabaseProperties(DatabaseProperties dbProps) {
        this.dbProps = dbProps;
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(dbProps.connectionString());
    }

    public UserDTO getUserByUsername(String username) {
        String sql = "SELECT USERNAME, PASSWORD FROM USERS WHERE USERNAME = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapper.mapToUserDTO(rs);
            }
        } catch (SQLException e) {
            throw new UserDataAccessException("Fout bij ophalen van gebruiker op basis van username.", e);
        }
        return null;
    }

    public String getUsernameByToken(String token) {
        String sql = "SELECT USERNAME FROM USERS WHERE TOKEN = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, token);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getString("USERNAME");
            }
        } catch (SQLException e) {
            throw new UserDataAccessException("Fout bij ophalen van username via token.", e);
        }
        return null;
    }

    public void updateUserToken(String username, String token) {
        String sql = "UPDATE USERS SET TOKEN = ? WHERE USERNAME = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, token);
            stmt.setString(2, username);
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new UserDataAccessException("Fout bij updaten van token voor gebruiker.", e);
        }
    }

    public boolean isTokenValid(String token) {
        return getUsernameByToken(token) != null;
    }
}
