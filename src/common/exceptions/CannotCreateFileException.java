package common.exceptions;
/**
 * thrown when unable to create file
 */
public class CannotCreateFileException extends FileException{
    private static final String message = "cannot create file";
    public CannotCreateFileException(){
        super(message);
    }
}
