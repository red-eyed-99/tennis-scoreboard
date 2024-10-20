package exceptions;

public class PlayerNameValidationException extends RuntimeException {

    public PlayerNameValidationException(String message) {
        super(message);
    }
}
