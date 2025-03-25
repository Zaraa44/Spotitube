package nl.han.oose.dea.rest.datasource.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.mindrot.jbcrypt.BCrypt;

public class InsertUserDAO {
    private String dbUrl;
    private String dbUser;
    private String dbPassword;

    public InsertUserDAO(String dbUrl, String dbUser, String dbPassword) {
        this.dbUrl = dbUrl;
        this.dbUser = dbUser;
        this.dbPassword = dbPassword;
    }

    public void insertUser(String username, String plainPassword) {
        String hashedPassword = hashPassword(plainPassword);
        String sql = "INSERT INTO USERS (USERNAME, PASSWORD, TOKEN) VALUES (?, ?, NULL)";

        Connection connection = null;

        try {
            connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
            PreparedStatement stmt = connection.prepareStatement(sql);

            stmt.setString(1, username);
            stmt.setString(2, hashedPassword);
            stmt.executeUpdate();
            System.out.println("User " + username + " has been inserted into the database.");

        } catch (SQLException e) {
            System.out.println("Error connecting to the database: " + e.getMessage());
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    System.out.println("Error closing connection: " + e.getMessage());
                }
            }
        }
    }

    private String hashPassword(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt(12));
    }

    public static void main(String[] args) {
        String dbUrl = "jdbc:sqlserver://localhost:1433;databaseName=Spotitube";
        String dbUser = "nisa";
        String dbPassword = "Naelbdp123!";

        InsertUserDAO userDAO = new InsertUserDAO(dbUrl, dbUser, dbPassword);

        userDAO.insertUser("user5", "password123");
        userDAO.insertUser("user6", "securepass456");
    }
}
