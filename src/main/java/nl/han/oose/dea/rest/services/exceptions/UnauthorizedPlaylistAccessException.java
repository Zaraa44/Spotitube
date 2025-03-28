package nl.han.oose.dea.rest.services.exceptions;


public class UnauthorizedPlaylistAccessException extends RuntimeException {
    public UnauthorizedPlaylistAccessException(String message) {
        super(message);
    }
}
