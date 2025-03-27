package nl.han.oose.dea.rest.datasource.DAO;

import nl.han.oose.dea.rest.datasource.mappers.TrackDAOMapper;
import nl.han.oose.dea.rest.services.dto.Track.TrackDTO;
import nl.han.oose.dea.rest.services.exceptions.Track.TrackNotFoundException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TrackDAO {
    private final String dbUrl = "jdbc:sqlserver://localhost:1433;databaseName=Spotitube";
    private final String dbUser = "nisa";
    private final String dbPassword = "Naelbdp123!";
    private final TrackDAOMapper mapper = new TrackDAOMapper();

    public List<TrackDTO> getTracksForPlaylist(int playlistId) {
        String sql = "SELECT * FROM TRACKS T JOIN PLAYLISTTRACKS PT ON T.TRACK_ID = PT.TRACK_ID WHERE PT.ID = ?";
        List<TrackDTO> tracks = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, playlistId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                tracks.add(mapper.mapToTrackDTO(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return tracks;
    }

    public List<TrackDTO> getTracksNotInPlaylist(int playlistId) {
        String sql = """
        SELECT * FROM TRACKS T
        WHERE T.TRACK_ID NOT IN (
            SELECT TRACK_ID FROM PLAYLISTTRACKS WHERE ID = ?
        )
        """;
        List<TrackDTO> tracks = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, playlistId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                tracks.add(mapper.mapToTrackDTO(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return tracks;
    }

    public void addTrackToPlaylist(int playlistId, int trackId, boolean offlineAvailable) {
        String sql = "INSERT INTO PLAYLISTTRACKS (ID, TRACK_ID, OFFLINEAVAILABLE) VALUES (?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, playlistId);
            stmt.setInt(2, trackId);
            stmt.setBoolean(3, offlineAvailable);

            int rows = stmt.executeUpdate();
            if (rows == 0) {
                throw new TrackNotFoundException("Track kon niet worden toegevoegd.");
            }

        } catch (SQLIntegrityConstraintViolationException e) {
            throw new TrackNotFoundException("Track bestaat niet of is ongeldig (FK constraint).");
        } catch (SQLException e) {
            throw new RuntimeException("Databasefout bij toevoegen track aan playlist.", e);
        }
    }

    public void removeTrackFromPlaylist(int playlistId, int trackId) {
        String sql = "DELETE FROM PLAYLISTTRACKS WHERE ID = ? AND TRACK_ID = ?";

        try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, playlistId);
            stmt.setInt(2, trackId);

            int rows = stmt.executeUpdate();
            if (rows == 0) {
                throw new TrackNotFoundException("Track zat niet in de playlist.");
            }

        } catch (SQLException e) {
            throw new RuntimeException("Databasefout bij verwijderen van track uit playlist.", e);
        }
    }
}
