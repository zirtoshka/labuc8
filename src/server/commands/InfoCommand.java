package server.commands;

import common.collection.LabWorkManager;
import common.commands.CommandImpl;
import common.commands.CommandType;

public class InfoCommand extends CommandImpl {
    private final LabWorkManager collectionManager;

    public InfoCommand(LabWorkManager cm) {
        super("info", CommandType.NORMAL);
        collectionManager = cm;//TODO
    }

    @Override
    public String execute() {
        return collectionManager.getInfo();
    }

}
