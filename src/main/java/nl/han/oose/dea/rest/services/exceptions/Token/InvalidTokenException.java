package nl.han.oose.dea.rest.services.exceptions.Token;


public class InvalidTokenException extends RuntimeException {
    public InvalidTokenException(String message) {
        super(message);
    }
}
