package server.commands;

import common.collection.LabWorkManager;
import common.commands.CommandImpl;
import common.commands.CommandType;
import common.connection.CollectionOperation;

public class AddIfMinCommand extends CommandImpl {
    private final LabWorkManager collectionManager;

    public AddIfMinCommand(LabWorkManager cm) {
        super("add_if_min", CommandType.NORMAL, CollectionOperation.ADD);
        collectionManager = cm;
    }

    @Override
    public String execute() {
        collectionManager.addIfMin(getWorkerArg());
        return ("Added element: " + getWorkerArg().toString());
    }

}
