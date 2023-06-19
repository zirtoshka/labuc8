package server.commands;

import common.collection.LabWorkManager;
import common.commands.CommandImpl;
import common.commands.CommandType;
import common.exceptions.EmptyCollectionException;

public class ShowCommand extends CommandImpl {
    private final LabWorkManager collectionManager;

    public ShowCommand(LabWorkManager cm) {
        super("show", CommandType.NORMAL);
        collectionManager = cm;
    }

    @Override
    public String execute() {
        if (collectionManager.getCollection().isEmpty()) throw new EmptyCollectionException();
        collectionManager.sort();
        return collectionManager.serializeCollection();
    }

}
