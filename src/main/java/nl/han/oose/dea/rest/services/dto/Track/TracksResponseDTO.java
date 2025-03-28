package nl.han.oose.dea.rest.services.dto.track;

import java.util.List;

public class TracksResponseDTO {
    private List<TrackDTO> tracks;

    public TracksResponseDTO(List<TrackDTO> tracks) {
        this.tracks = tracks;
    }

    public List<TrackDTO> getTracks() {
        return tracks;
    }
}
