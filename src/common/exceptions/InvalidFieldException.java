package common.exceptions;
/**
 * thrown when field is invalid
 */
public class InvalidFieldException extends InvalidDataException {
    private static final String message = "invalid field in class";
    public InvalidFieldException(){
        super(message);
    }
}
