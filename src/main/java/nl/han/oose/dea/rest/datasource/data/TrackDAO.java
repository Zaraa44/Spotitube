package nl.han.oose.dea.rest.datasource.data;

import nl.han.oose.dea.rest.services.dto.Track.TrackDTO;
import nl.han.oose.dea.rest.services.exceptions.Track.TrackNotFoundException;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class TrackDAO {
    private final String dbUrl = "jdbc:sqlserver://localhost:1433;databaseName=Spotitube";
    private final String dbUser = "nisa";
    private final String dbPassword = "Naelbdp123!";

    public List<TrackDTO> getTracksForPlaylist(int playlistId) {
        String sql = "SELECT T.TRACK_ID, T.TITLE, T.PERFORMER, T.DURATION, T.ALBUM, " +
                "T.PLAYCOUNT, T.PUBLICATIONDATE, T.DESCRIPTION, T.OFFLINEAVAILABLE " +
                "FROM TRACKS T " +
                "JOIN PLAYLISTTRACKS PT ON T.TRACK_ID = PT.TRACK_ID " +
                "WHERE PT.ID = ?";

        List<TrackDTO> tracks = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");

        try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, playlistId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String formattedDate = null;
                Timestamp pubDate = rs.getTimestamp("PUBLICATIONDATE");
                if (pubDate != null) {
                    formattedDate = dateFormat.format(pubDate);
                }

                tracks.add(new TrackDTO(
                        rs.getInt("TRACK_ID"),
                        rs.getString("TITLE").trim(),
                        rs.getString("PERFORMER").trim(),
                        rs.getInt("DURATION"),
                        rs.getString("ALBUM") != null ? rs.getString("ALBUM").trim() : null,
                        rs.getObject("PLAYCOUNT") != null ? rs.getInt("PLAYCOUNT") : null,
                        formattedDate,
                        rs.getString("DESCRIPTION") != null ? rs.getString("DESCRIPTION").trim() : null,
                        rs.getBoolean("OFFLINEAVAILABLE")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return tracks;
    }

    public List<TrackDTO> getTracksNotInPlaylist(int playlistId) {
        String sql = """
        SELECT T.TRACK_ID, T.TITLE, T.PERFORMER, T.DURATION, T.ALBUM,
               T.PLAYCOUNT, T.PUBLICATIONDATE, T.DESCRIPTION, T.OFFLINEAVAILABLE
        FROM TRACKS T
        WHERE T.TRACK_ID NOT IN (
            SELECT TRACK_ID FROM PLAYLISTTRACKS WHERE ID = ?
        )
        """;

        List<TrackDTO> tracks = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");

        try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, playlistId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String formattedDate = null;
                Timestamp pubDate = rs.getTimestamp("PUBLICATIONDATE");
                if (pubDate != null) {
                    formattedDate = dateFormat.format(pubDate);
                }

                tracks.add(new TrackDTO(
                        rs.getInt("TRACK_ID"),
                        rs.getString("TITLE").trim(),
                        rs.getString("PERFORMER").trim(),
                        rs.getInt("DURATION"),
                        rs.getString("ALBUM") != null ? rs.getString("ALBUM").trim() : null,
                        rs.getObject("PLAYCOUNT") != null ? rs.getInt("PLAYCOUNT") : null,
                        formattedDate,
                        rs.getString("DESCRIPTION") != null ? rs.getString("DESCRIPTION").trim() : null,
                        rs.getBoolean("OFFLINEAVAILABLE")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return tracks;
    }
    public void addTrackToPlaylist(int playlistId, int trackId, boolean offlineAvailable) {
        System.out.printf("DAO: poging tot INSERT → playlistId=%d, trackId=%d, offline=%s%n",
                playlistId, trackId, offlineAvailable);

        String sql = "INSERT INTO PLAYLISTTRACKS (ID, TRACK_ID, OFFLINEAVAILABLE) VALUES (?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, playlistId);
            stmt.setInt(2, trackId);
            stmt.setBoolean(3, offlineAvailable);

            int rows = stmt.executeUpdate();

            if (rows == 0) {
                throw new TrackNotFoundException("Track kon niet worden toegevoegd. Bestaat deze wel?");
            }

            System.out.println("Track toegevoegd aan database, rows affected = " + rows);

        } catch (SQLIntegrityConstraintViolationException e) {
            throw new TrackNotFoundException("Track bestaat niet of is ongeldig (FK constraint).");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Databasefout bij toevoegen track aan playlist.");
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

            System.out.printf("Track verwijderd: playlistId=%d, trackId=%d, rows=%d%n", playlistId, trackId, rows);

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Databasefout bij verwijderen van track uit playlist.");
        }
    }




}
