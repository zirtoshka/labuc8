package common.exceptions;
/**
 * thrown when number is incorrect
 */
public class InvalidNumberException extends InvalidDataException {
    private static final String message = "invalid number format";
    public InvalidNumberException(){
        super(message);
    }
    public InvalidNumberException(String msg){
        super(msg);
    }
}
