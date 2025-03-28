package nl.han.oose.dea.rest.datasource.DAO;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import nl.han.oose.dea.rest.datasource.mappers.UserDAOMapper;
import nl.han.oose.dea.rest.services.dto.User.UserDTO;

import java.sql.*;

@ApplicationScoped
public class UserDAO {

    private final String dbUrl = "jdbc:sqlserver://localhost:1433;databaseName=Spotitube";
    private final String dbUser = "nisa";
    private final String dbPassword = "Naelbdp123!";

    private UserDAOMapper mapper;

    public UserDAO() {
    }

    @Inject
    public void setMapper(UserDAOMapper mapper) {
        this.mapper = mapper;
    }

    public UserDTO getUserByUsername(String username) {
        String sql = "SELECT USERNAME, PASSWORD FROM USERS WHERE USERNAME = ?";

        try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapper.mapToUserDTO(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getUsernameByToken(String token) {
        String sql = "SELECT USERNAME FROM USERS WHERE TOKEN = ?";

        try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, token);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getString("USERNAME");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void updateUserToken(String username, String token) {
        String sql = "UPDATE USERS SET TOKEN = ? WHERE USERNAME = ?";

        try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, token);
            stmt.setString(2, username);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean isTokenValid(String token) {
        return getUsernameByToken(token) != null;
    }
}
