package nl.han.oose.dea.rest.services.exceptions.Playlist;


public class UnauthorizedPlaylistAccessException extends RuntimeException {
    public UnauthorizedPlaylistAccessException(String message) {
        super(message);
    }
}
