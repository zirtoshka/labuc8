package server.commands;

import common.commands.core.CommandImpl;
import common.commands.core.CommandType;
import common.connection.CollectionOperation;
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
    public CollectionOperation getCollectionOperation() {
        return CollectionOperation.ADD;
    }

    @Override
    public String execute() throws InvalidDataException, CommandException {
        if (!hasLabWorkArg()) throw new CheckIdException();
        collectionManager.add(getLabWorkArg());
        return "Added element: " + getLabWorkArg().toString();
    }
}
