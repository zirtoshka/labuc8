package common.exceptions;

/**
 * thrown when command argument is invalid
 */
public class InvalidCommandArgumentException extends CommandException{
    private static final String message = "command argument is invalid";
    public InvalidCommandArgumentException(String msg) {
        super(msg);
    }
    public InvalidCommandArgumentException() {
        super(message);
    }
}
