package nl.han.oose.dea.rest.datasource.data;

import nl.han.oose.dea.rest.services.dto.Playlist.PlaylistDTO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PlaylistDAO {
    private String dbUrl = "jdbc:sqlserver://localhost:1433;databaseName=Spotitube";
    private String dbUser = "nisa";
    private String dbPassword = "Naelbdp123!";

    public List<PlaylistDTO> getAllPlaylists(String requestingUser) {
        String sql = "SELECT ID, NAME, USERNAME, OWNER FROM PLAYLIST";
        List<PlaylistDTO> playlists = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("ID");
                String name = rs.getString("NAME").trim();
                String ownerUsername = rs.getString("USERNAME").trim();
                boolean isOwner = rs.getBoolean("OWNER"); // waarde 1 = true, 0 = false

                playlists.add(new PlaylistDTO(id, name, isOwner));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return playlists;
    }



    public int calculateTotalLength() {
        String sql = "SELECT SUM(LENGTH) AS TOTAL FROM PLAYLIST";
        try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("TOTAL");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void addPlaylist(String name, String ownerUsername) {
        String sql = "INSERT INTO PLAYLIST (ID, USERNAME, NAME, OWNER, LENGTH) VALUES (?, ?, ?, ?, 0)";

        int id = generateUniqueId();

        try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.setString(2, ownerUsername);
            stmt.setString(3, name);
            stmt.setBoolean(4, true);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private int generateUniqueId() {
        String sql = "SELECT MAX(ID) AS MAX_ID FROM PLAYLIST";
        try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
             Statement stmt = conn.createStatement()) {

            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                return rs.getInt("MAX_ID") + 1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 1;
    }

    public void updatePlaylistName(int id, String newName) {
        String sql = "UPDATE PLAYLIST SET NAME = ? WHERE ID = ?";

        try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, newName);
            stmt.setInt(2, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deletePlaylist(int id) {
        String sql = "DELETE FROM PLAYLIST WHERE ID = ?";

        try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean isOwnerOfPlaylist(String token, int playlistId) {
        String sql = """
        SELECT P.ID
        FROM PLAYLIST P
        JOIN USERS U ON P.USERNAME = U.USERNAME
        WHERE P.ID = ? AND U.TOKEN = ?
    """;

        try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, playlistId);
            stmt.setString(2, token);

            ResultSet rs = stmt.executeQuery();
            return rs.next(); // als een record wordt gevonden, klopt de eigenaar
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void updateOwnerColumnForUser(String username) {
        String resetAll = "UPDATE PLAYLIST SET OWNER = 0";
        String setOwner = "UPDATE PLAYLIST SET OWNER = 1 WHERE USERNAME = ?";

        try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword)) {

            try (PreparedStatement resetStmt = conn.prepareStatement(resetAll)) {
                resetStmt.executeUpdate();
            }
            
            try (PreparedStatement setOwnerStmt = conn.prepareStatement(setOwner)) {
                setOwnerStmt.setString(1, username);
                setOwnerStmt.executeUpdate();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }




}
