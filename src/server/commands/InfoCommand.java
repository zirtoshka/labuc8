package server.commands;




import common.collection.LabWorkManagerImpl;
import common.commands.core.CommandImpl;
import common.commands.core.CommandType;
import common.exceptions.*;
import common.data.*;
import server.collection.LabWorkCollectionManager;

public class InfoCommand extends CommandImpl {
    private LabWorkCollectionManager collectionManager;
    public InfoCommand(LabWorkCollectionManager labWorkCollectionManager){
        super("info", CommandType.NORMAL);
        collectionManager = labWorkCollectionManager;
    }

    @Override
    public String execute() throws InvalidDataException {
        return collectionManager.getInfo();
    }
}
