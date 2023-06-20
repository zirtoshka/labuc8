package common.exceptions;
/**
 * thrown when collection is empty
 */
public class EmptyCollectionException extends CommandException{
    private static final String message = "collection is empty";
    public EmptyCollectionException(){
        super(message);
    }
}
