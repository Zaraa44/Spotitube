package nl.han.oose.dea.rest.datasource.mappers;

import jakarta.enterprise.context.ApplicationScoped;
import nl.han.oose.dea.rest.services.dto.track.TrackDTO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

@ApplicationScoped
public class TrackDAOMapper {

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");

    public TrackDTO mapToTrackDTO(ResultSet rs) throws SQLException {
        String formattedDate = null;
        Timestamp pubDate = rs.getTimestamp("PUBLICATIONDATE");
        if (pubDate != null) {
            formattedDate = dateFormat.format(pubDate);
        }

        Integer playcount = null;
        if (rs.getObject("PLAYCOUNT") != null) {
            playcount = rs.getInt("PLAYCOUNT");
        }

        return new TrackDTO(
                rs.getInt("TRACK_ID"),
                rs.getString("TITLE").trim(),
                rs.getString("PERFORMER").trim(),
                rs.getInt("DURATION"),
                rs.getString("ALBUM") != null ? rs.getString("ALBUM").trim() : null,
                playcount,
                formattedDate,
                rs.getString("DESCRIPTION") != null ? rs.getString("DESCRIPTION").trim() : null,
                rs.getBoolean("OFFLINEAVAILABLE")
        );
    }
}
