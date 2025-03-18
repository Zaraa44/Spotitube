package nl.han.oose.dea.rest.datasource.data;

import nl.han.oose.dea.rest.services.dto.User.UserDTO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {
    private static final String URL = "jdbc:sqlserver://localhost:1433;databaseName=Spotitube";
    private static final String USER = "nisa";
    private static final String PASSWORD = "Naelbdp123!";

    public UserDTO getUserByUsername(String username) {
        String query = "SELECT USERNAME, PASSWORD FROM \"USERS\" WHERE USERNAME = ?";

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return new UserDTO(
                        resultSet.getString("USERNAME"),
                        resultSet.getString("PASSWORD")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
