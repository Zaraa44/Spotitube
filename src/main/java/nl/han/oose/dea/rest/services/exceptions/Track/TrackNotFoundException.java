package nl.han.oose.dea.rest.services.exceptions.Track;

public class TrackNotFoundException extends RuntimeException {
  public TrackNotFoundException(String message) {
    super(message);
  }
}