package common.exceptions;
/**
 * thrown when user enters invalid command
 */
public class NoSuchCommandException extends CommandException{
    private static final String message = "wrong command";
    public NoSuchCommandException() {
        super(message);
    }
}
