package nl.han.oose.dea.rest.services.exceptions;

public class PlaylistDataAccessException extends RuntimeException {
  public PlaylistDataAccessException(String message, Throwable cause) {
    super(message, cause);
  }

  public PlaylistDataAccessException(String message) {
    super(message);
  }
}
