package common.exceptions;
/**
 * thrown  when user does not enter required command argument
 */
public class MissedCommandArgumentException extends InvalidCommandArgumentException{
    private static final String message = "missed command argument";
    public MissedCommandArgumentException(){
        super(message);
    }
}
