package common.exceptions;
/**
 * thrown when date format is invalid
 */
public class InvalidDateFormatException extends InvalidDataException {
    private static final String message = "date format must be yyyy-MM-dd";
    public InvalidDateFormatException(){
        super(message);
    }
}
