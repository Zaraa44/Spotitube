package nl.han.oose.dea.rest.datasource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

public class SQLDatabaseConnection {
    public static void main(String[] args) {
        // Pas de connectiestring aan met de juiste server, database en inloggegevens.
        String connectionUrl = "jdbc:sqlserver://localhost;"
                + "databaseName=Spotitube;"
                + "user=LAPTOP-UCNV2OLS/Nisa;"
                + "encrypt=true;"
                + "trustServerCertificate=false;"
                + "loginTimeout=30;";

        // SQL statement om een waarde in de TRACK-tabel in te voegen.
        String sqlInsert = "INSERT INTO TRACK (TRACKID, TITLE, PERFORMER, DURATION, ALBUM, PLAYCOUNT, PUBLICATIONDATE, DESCRIPTION, OFFLINEAVAILABLE) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(connectionUrl);
             PreparedStatement pstmt = conn.prepareStatement(sqlInsert)) {

            // Stel hier de waarden in. Zorg ervoor dat de TRACKID uniek is.
            pstmt.setInt(1, 100);  // Voorbeeld TRACKID, pas aan indien nodig.
            pstmt.setString(2, "Test Song");
            pstmt.setString(3, "Test Artist");
            pstmt.setInt(4, 180);  // Duur in seconden.
            pstmt.setString(5, "Test Album");
            pstmt.setInt(6, 0);  // Begin met een playcount van 0.
            pstmt.setTimestamp(7, new Timestamp(System.currentTimeMillis()));  // Huidige datum/tijd als publicatiedatum.
            pstmt.setString(8, "Test Description");
            pstmt.setBoolean(9, true);  // Bijvoorbeeld, beschikbaar offline.

            // Voer de insert uit en geef het aantal toegevoegde rijen weer.
            int rowsAffected = pstmt.executeUpdate();
            System.out.println("Aantal toegevoegde rijen: " + rowsAffected);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
