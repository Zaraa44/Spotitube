package nl.han.oose.dea.rest.datasource.dao;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import nl.han.oose.dea.rest.datasource.mappers.TrackDAOMapper;
import nl.han.oose.dea.rest.datasource.util.DatabaseProperties;
import nl.han.oose.dea.rest.services.dto.track.TrackDTO;
import nl.han.oose.dea.rest.services.exceptions.TrackDataAccessException;
import nl.han.oose.dea.rest.services.exceptions.TrackNotFoundException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class TrackDAO {

    private TrackDAOMapper mapper;
    private DatabaseProperties dbProps;

    public TrackDAO() {
    }

    @Inject
    private PlaylistDAO playlistDAO;

    @Inject
    public void setMapper(TrackDAOMapper mapper) {
        this.mapper = mapper;
    }

    @Inject
    public void setDatabaseProperties(DatabaseProperties dbProps) {
        this.dbProps = dbProps;
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(dbProps.connectionString());
    }

    public List<TrackDTO> getTracksForPlaylist(int playlistId) {
        String sql = "SELECT * FROM TRACKS T JOIN PLAYLISTTRACKS PT ON T.TRACK_ID = PT.TRACK_ID WHERE PT.ID = ?";
        List<TrackDTO> tracks = new ArrayList<>();

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, playlistId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                tracks.add(mapper.mapToTrackDTO(rs));
            }

        } catch (SQLException e) {
            throw new TrackDataAccessException("Fout bij ophalen van tracks in playlist.", e);
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

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, playlistId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                tracks.add(mapper.mapToTrackDTO(rs));
            }

        } catch (SQLException e) {
            throw new TrackDataAccessException("Fout bij ophalen van tracks buiten playlist.", e);
        }

        return tracks;
    }

    public void addTrackToPlaylist(int playlistId, int trackId, boolean offlineAvailable) {
        String sql = "INSERT INTO PLAYLISTTRACKS (ID, TRACK_ID, OFFLINEAVAILABLE) VALUES (?, ?, ?)";

        try (Connection conn = getConnection();
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
            throw new TrackDataAccessException("Fout bij toevoegen van track aan playlist.", e);
        }
    }

    public void removeTrackFromPlaylist(int playlistId, int trackId) {
        String sql = "DELETE FROM PLAYLISTTRACKS WHERE ID = ? AND TRACK_ID = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, playlistId);
            stmt.setInt(2, trackId);

            int rows = stmt.executeUpdate();
            if (rows == 0) {
                throw new TrackNotFoundException("Track zat niet in de playlist.");
            }

        } catch (SQLException e) {
            throw new TrackDataAccessException("Fout bij verwijderen van track uit playlist.", e);
        }
    }
}
