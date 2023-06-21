package server.commands;

import common.commands.core.CommandImpl;
import common.commands.core.CommandType;
import common.connection.CollectionOperation;
import common.exceptions.*;
import server.collection.CollectionManager;
import common.data.LabWork;
import common.io.InputManager;

import static common.utils.Parser.parseId;

public class RemoveLowerCommand extends CommandImpl {
    private final CollectionManager<LabWork> collectionManager;

    public RemoveLowerCommand(CollectionManager<LabWork> cm){
        super("remove_lower", CommandType.NORMAL);
        collectionManager = cm;
    }

    @Override
    public CollectionOperation getCollectionOperation() {
        return CollectionOperation.REMOVE;
    }

    @Override
    public String execute() throws InvalidDataException {
        if(collectionManager.getCollection().isEmpty()) throw new EmptyCollectionException();

        boolean success = collectionManager.removeLower(getLabWorkArg());
        if (success) return "some elements have been removed";
        else throw new CommandException("no elements found to remove");
    }
}
