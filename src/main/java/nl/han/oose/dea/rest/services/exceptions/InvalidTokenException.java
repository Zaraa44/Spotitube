package nl.han.oose.dea.rest.services.exceptions;


public class InvalidTokenException extends RuntimeException {
    public InvalidTokenException(String message) {
        super(message);
    }
}
