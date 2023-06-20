package server.exceptions;

import common.exceptions.CommandException;

/**
 * thrown if command needed check id
 */
public class CheckIdException extends CommandException {
    public CheckIdException(){
        super("this command need check id");
    }
}
