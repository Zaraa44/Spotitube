package nl.han.oose.dea.rest.datasource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLDatabaseConnection {
    private static final String URL = "jdbc:sqlserver://localhost:1433;databaseName=Spotitube";
    private static final String USER = "nisa";
    private static final String PASSWORD = "Naelbdp123!";

    public static void main(String[] args) {

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            if (connection != null) {
                System.out.println("Connected to SQL Server successfully!");
            }
        } catch (SQLException e) {
            System.err.println("Connection failed: " + e.getMessage());
        }
    }
}
