package server.commands;

import common.auth.User;

import common.commands.core.CommandImpl;
import common.commands.core.CommandType;
import common.connection.CollectionOperation;
import common.data.LabWork;
import common.exceptions.EmptyCollectionException;
import common.exceptions.InvalidDataException;
import server.collection.CollectionManager;
import server.database.LabWorkDatabaseManager;

public class ClearCommand extends CommandImpl {
    private final LabWorkDatabaseManager collectionManager;

    public ClearCommand(CollectionManager<LabWork> cm) {
        super("clear", CommandType.NORMAL);
        collectionManager = (LabWorkDatabaseManager) cm;
    }

    @Override
    public CollectionOperation getCollectionOperation() {
        return CollectionOperation.CLEAR;
    }

    @Override
    public String execute() throws InvalidDataException {
        if (collectionManager.getCollection().isEmpty()) throw new EmptyCollectionException();
        User user = getArgument().getUser();
        collectionManager.clear(user);
        return "collection cleared";
    }

}
