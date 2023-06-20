package server.commands;

import common.collection.LabWorkManager;
import common.commands.CommandImpl;
import common.commands.CommandType;
import common.connection.CollectionOperation;

public class AddIfMaxCommand extends CommandImpl {
    private final LabWorkManager collectionManager;

    public AddIfMaxCommand(LabWorkManager cm) {
        super("add_if_max", CommandType.NORMAL, CollectionOperation.ADD);
        collectionManager = cm;
    }

    @Override
    public String execute() {
        collectionManager.addIfMax(getLabWork());
        return ("Added element: " + getLabWork().toString());
    }
}
