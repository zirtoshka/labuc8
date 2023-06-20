package common.exceptions;
/**
 * thrown when entered character is unprocessed
 */
public class InvalidInputCharacterException extends RuntimeException{
    private static final String message = "somewhere entered character is unprocessed\nincorrect termination of the program";
    public InvalidInputCharacterException() {
        super(message);
    }
    public InvalidInputCharacterException(String message) {
        super(message);
    }
}
