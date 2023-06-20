package server.commands;


import common.commands.core.CommandImpl;
import common.commands.core.CommandType;
import common.exceptions.*;
import common.data.*;
import server.collection.CollectionManager;
import server.exceptions.CheckIdException;

public class AddIfMaxCommand extends CommandImpl {
    private CollectionManager<LabWork> collectionManager;
    public AddIfMaxCommand(CollectionManager<LabWork> cm){
        super("add_if_max", CommandType.NORMAL);
        collectionManager = cm;
    }

    @Override
    public String execute(){
        if (!hasLabWorkArg()) throw new CheckIdException();
        collectionManager.addIfMax(getLabWorkArg());
        return ("Added element: " + getLabWorkArg().toString());
    }
}
