package nl.han.oose.dea.rest.services.dto.playlist;

import java.util.List;

public class PlaylistDTO {
    private int id;
    private String name;
    private boolean owner;
    private List<Object> tracks;

    public PlaylistDTO() {

    }

    public PlaylistDTO(int id, String name, boolean owner) {
        this.id = id;
        this.name = name;
        this.owner = owner;
        this.tracks = List.of();
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public boolean isOwner() { return owner; }
    public void setOwner(boolean owner) { this.owner = owner; }

    public List<Object> getTracks() { return tracks; }
    public void setTracks(List<Object> tracks) { this.tracks = tracks; }
}
