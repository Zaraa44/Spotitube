package nl.han.oose.dea.rest.services.exceptions;

public class DatabaseException extends RuntimeException {
  public DatabaseException() {
    super("Kon database.properties niet laden");
  }
}
