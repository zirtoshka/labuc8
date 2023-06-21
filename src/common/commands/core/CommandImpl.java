package common.commands.core;

import common.connection.*;
import common.data.LabWork;
import common.exceptions.*;
import server.exceptions.CheckIdException;

/**
 * basic command implementation
 */
public abstract class CommandImpl implements Command{
    private CommandType type;
    private String name;
    private Request arg;
    public CommandImpl(String n, CommandType t){
        name = n;
        type = t;
    }
    public CommandType getType(){
        return type;
    }
    public String getName(){
        return name;
    }

    @Override
    public CollectionOperation getCollectionOperation() {
        return CollectionOperation.NONE;
    }

    /**
     * custom execute command
     *
     * @return String
     * @throws InvalidDataException
     * @throws CommandException
     * @throws FileException
     * @throws ConnectionException
     */
    public String execute() throws InvalidDataException, CommandException, FileException, ConnectionException, CollectionException {
        return "";
    }

    /**
     * wraps execute into response
     *
     * @return response
     */
    @Override
    public Response run() throws InvalidDataException, CommandException, FileException, ConnectionException, CollectionException {
        AnswerMsg res = new AnswerMsg();
        res.info(execute());
        return res;
    }
    public Request getArgument(){
        return arg;
    }
    public void setArgument(Request req){
        arg=req;
    }
    public boolean hasStringArg(){
        return arg!=null && arg.getStringArg()!=null && !arg.getStringArg().equals("");
    }
    public boolean hasLabWorkArg(){
        return arg!=null && arg.getLabWork()!=null;
    }

    public String getStringArg(){
        return getArgument().getStringArg();
    }

    public LabWork getLabWorkArg(){
        return getArgument().getLabWork();
    }
}
