package common.exceptions;
/**
 * thrown when script call loops
 */
public class RecursiveScriptExecuteException extends CommandException{
    private static final String message = "recursive script execute attempt";
    public RecursiveScriptExecuteException(){
        super(message);
    }
}
