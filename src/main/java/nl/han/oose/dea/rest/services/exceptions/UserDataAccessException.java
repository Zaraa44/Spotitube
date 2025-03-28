package nl.han.oose.dea.rest.services.exceptions;

public class UserDataAccessException extends RuntimeException {
    public UserDataAccessException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserDataAccessException(String message) {
        super(message);
    }
}