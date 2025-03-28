package nl.han.oose.dea.rest.datasource.dao;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import nl.han.oose.dea.rest.datasource.mappers.PlaylistDAOMapper;
import nl.han.oose.dea.rest.datasource.util.DatabaseProperties;
import nl.han.oose.dea.rest.services.dto.playlist.PlaylistDTO;
import nl.han.oose.dea.rest.services.exceptions.PlaylistDataAccessException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class PlaylistDAO {

    private PlaylistDAOMapper mapper;
    private DatabaseProperties dbProps;

    public PlaylistDAO() {
    }

    @Inject
    public void setMapper(PlaylistDAOMapper mapper) {
        this.mapper = mapper;
    }

    @Inject
    public void setDatabaseProperties(DatabaseProperties dbProps) {
        this.dbProps = dbProps;
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(dbProps.connectionString());
    }

    public List<PlaylistDTO> getAllPlaylists(String requestingUser) {
        List<PlaylistDTO> playlists = new ArrayList<>();
        String sql = "SELECT ID, NAME, USERNAME, OWNER FROM PLAYLIST";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                playlists.add(mapper.mapToPlaylistDTO(rs));
            }

        } catch (SQLException e) {
            throw new PlaylistDataAccessException("Fout bij ophalen van alle playlists.", e);
        }

        return playlists;
    }

    public int calculateTotalLength() {
        String sql = "SELECT SUM(LENGTH) AS TOTAL FROM PLAYLIST";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                return rs.getInt("TOTAL");
            }

        } catch (SQLException e) {
            throw new PlaylistDataAccessException("Fout bij uitrekenen.", e);
        }


        return 0;
    }

    public void addPlaylist(String name, String ownerUsername) {
        String sql = "INSERT INTO PLAYLIST (ID, USERNAME, NAME, OWNER, LENGTH) VALUES (?, ?, ?, ?, 0)";
        int id = generateUniqueId();

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.setString(2, ownerUsername);
            stmt.setString(3, name);
            stmt.setBoolean(4, true);
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new PlaylistDataAccessException("Fout bij toevoegen van playlist.", e);
        }
    }

    private int generateUniqueId() {
        String sql = "SELECT MAX(ID) AS MAX_ID FROM PLAYLIST";

        try (Connection conn = getConnection();
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

        try (Connection conn = getConnection();
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

        try (Connection conn = getConnection()) {

            try (PreparedStatement stmtTracks = conn.prepareStatement(deleteTracksSql)) {
                stmtTracks.setInt(1, id);
                stmtTracks.executeUpdate();
            }

            try (PreparedStatement stmtPlaylist = conn.prepareStatement(deletePlaylistSql)) {
                stmtPlaylist.setInt(1, id);
                stmtPlaylist.executeUpdate();
            }

        } catch (SQLException e) {
            throw new PlaylistDataAccessException("Fout bij verwijderen van playlist met tracks.", e);
        }

    }

    public boolean isOwnerOfPlaylist(String token, int playlistId) {
        String sql = """
                SELECT P.ID
                FROM PLAYLIST P
                JOIN USERS U ON P.USERNAME = U.USERNAME
                WHERE P.ID = ? AND U.TOKEN = ?
                """;

        try (Connection conn = getConnection();
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

        try (Connection conn = getConnection()) {

            try (PreparedStatement resetStmt = conn.prepareStatement(resetAll)) {
                resetStmt.executeUpdate();
            }

            try (PreparedStatement setOwnerStmt = conn.prepareStatement(setOwner)) {
                setOwnerStmt.setString(1, username);
                setOwnerStmt.executeUpdate();
            }

        } catch (SQLException e) {
            throw new PlaylistDataAccessException("Fout bij updaten.", e);
        }
    }


}
