package server.commands;


import common.commands.core.CommandImpl;
import common.commands.core.CommandType;
import common.exceptions.*;
import common.data.*;
import server.collection.CollectionManager;

public class ShowCommand extends CommandImpl {
    private CollectionManager<LabWork> collectionManager;
    public ShowCommand(CollectionManager<LabWork> cm){
        super("show", CommandType.NORMAL);
        collectionManager = cm;
    }

    @Override
    public String execute(){
        if (collectionManager.getCollection().isEmpty()) throw new EmptyCollectionException();
        collectionManager.sort();
        return collectionManager.serializeCollection();
    }
}
