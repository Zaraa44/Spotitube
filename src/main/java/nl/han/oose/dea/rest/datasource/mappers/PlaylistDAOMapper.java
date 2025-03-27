package nl.han.oose.dea.rest.datasource.mappers;

import nl.han.oose.dea.rest.services.dto.Playlist.PlaylistDTO;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PlaylistDAOMapper {
    public PlaylistDTO mapToPlaylistDTO(ResultSet rs) throws SQLException {
        int id = rs.getInt("ID");
        String name = rs.getString("NAME").trim();
        String ownerUsername = rs.getString("USERNAME").trim();
        boolean isOwner = rs.getBoolean("OWNER");
        return new PlaylistDTO(id, name, isOwner);
    }
}
