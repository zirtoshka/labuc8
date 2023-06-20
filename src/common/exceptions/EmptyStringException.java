package common.exceptions;

/**
 * thrown when user input is empty
 */
public class EmptyStringException extends InvalidDataException {
    private static final String message = "string can not be empty";
    public EmptyStringException(){
        super(message);
    }
}
