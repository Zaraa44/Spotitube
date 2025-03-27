package nl.han.oose.dea.rest.datasource.DAO;

import nl.han.oose.dea.rest.datasource.mappers.PlaylistDAOMapper;
import nl.han.oose.dea.rest.services.dto.Playlist.PlaylistDTO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PlaylistDAO {
    private final String dbUrl = "jdbc:sqlserver://localhost:1433;databaseName=Spotitube";
    private final String dbUser = "nisa";
    private final String dbPassword = "Naelbdp123!";

    private final PlaylistDAOMapper mapper;

    public PlaylistDAO() {
        this.mapper = new PlaylistDAOMapper();
    }

    public PlaylistDAO(PlaylistDAOMapper mapper) {
        this.mapper = mapper;
    }

    public List<PlaylistDTO> getAllPlaylists(String requestingUser) {
        List<PlaylistDTO> playlists = new ArrayList<>();
        String sql = "SELECT ID, NAME, USERNAME, OWNER FROM PLAYLIST";

        try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                playlists.add(mapper.mapToPlaylistDTO(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return playlists;
    }

    public int calculateTotalLength() {
        String sql = "SELECT SUM(LENGTH) AS TOTAL FROM PLAYLIST";

        try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

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
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

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
        String deleteTracksSql = "DELETE FROM PLAYLISTTRACKS WHERE ID = ?";
        String deletePlaylistSql = "DELETE FROM PLAYLIST WHERE ID = ?";

        try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword)) {

            try (PreparedStatement stmtTracks = conn.prepareStatement(deleteTracksSql)) {
                stmtTracks.setInt(1, id);
                stmtTracks.executeUpdate();
            }

            try (PreparedStatement stmtPlaylist = conn.prepareStatement(deletePlaylistSql)) {
                stmtPlaylist.setInt(1, id);
                stmtPlaylist.executeUpdate();
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Fout bij verwijderen van playlist met tracks.");
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
            return rs.next();

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
