package nl.han.oose.dea.rest.resources;

import jakarta.ws.rs.core.Response;
import nl.han.oose.dea.rest.services.dto.Playlist.PlaylistDTO;
import nl.han.oose.dea.rest.services.dto.Track.TrackDTO;
import nl.han.oose.dea.rest.services.dto.Track.TracksResponseDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class TrackResourceTest {
    private TrackResource sut;

    @BeforeEach
    void setup() {
        sut = new TrackResource();

    }

}
