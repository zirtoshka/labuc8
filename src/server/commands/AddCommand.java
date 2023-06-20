package server.commands;

import common.command.core.CommandImpl;
import common.command.core.CommandType;
import common.exceptions.*;
import common.data.*;
import server.collection.CollectionManager;
import server.exceptions.CheckIdException;

public class AddCommand extends CommandImpl {
    private CollectionManager<LabWork> collectionManager;
    public AddCommand(CollectionManager<LabWork> cm){
        super("add", CommandType.NORMAL);
        collectionManager = cm;
    }

    @Override
    public String execute() throws InvalidDataException, CommandException {
        if (!hasLabWorkArg()) throw new CheckIdException();
        collectionManager.add(getLabWorkArg());
        return "Added element: " + getLabWorkArg().toString();
    }
}