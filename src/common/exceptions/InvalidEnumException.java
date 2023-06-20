package common.exceptions;
/**
 * thrown when input does not match enum
 */
public class InvalidEnumException extends InvalidDataException{
    private static final String message = "wrong constant";
    public InvalidEnumException(){
        super(message);
    }
}
