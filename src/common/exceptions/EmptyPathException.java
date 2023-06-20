package common.exceptions;
/**
 * thrown when path is empty
 */
public class EmptyPathException extends FileException{
    private static final String message = "path is empty";
    public EmptyPathException(){
        super(message);
    }
}
