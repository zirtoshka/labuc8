package server.commands;


import common.commands.core.CommandImpl;
import common.commands.core.CommandType;
import common.exceptions.ExitException;
public class ExitCommand extends CommandImpl {
    public ExitCommand(){
        super("exit", CommandType.NORMAL);
    }
    @Override
    public String execute(){
        throw new ExitException();
    }
}
