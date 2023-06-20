package server.commands;

import common.commands.core.CommandImpl;
import common.commands.core.CommandType;
import common.exceptions.*;
import server.collection.CollectionManager;
import common.data.LabWork;

public class MaxByDisciplineCommand extends CommandImpl {
    private final CollectionManager<LabWork> collectionManager;

    public MaxByDisciplineCommand(CollectionManager<LabWork> cm){
        super("max_by_discipline", CommandType.NORMAL);
        collectionManager = cm;
    }

    @Override
    public String execute() throws InvalidDataException {
        if(collectionManager.getCollection().isEmpty()) throw new EmptyCollectionException();
        return collectionManager.maxByDiscipline();
    }
}
