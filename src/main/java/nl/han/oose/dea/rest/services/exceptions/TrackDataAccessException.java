package nl.han.oose.dea.rest.services.exceptions;

public class TrackDataAccessException extends RuntimeException {
    public TrackDataAccessException(String message, Throwable cause) {
        super(message, cause);
    }

    public TrackDataAccessException(String message) {
        super(message);
    }
}
