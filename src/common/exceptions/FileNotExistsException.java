package common.exceptions;
/**
 * thrown when file does not exist
 */
public class FileNotExistsException extends FileException{
    private static final String message = "cannot find file";
    public FileNotExistsException(){
        super(message);
    }
}
