package server.commands;

import common.commands.core.CommandImpl;
import common.commands.core.CommandType;
import common.exceptions.InvalidDataException;
import server.collection.CollectionManager;
import common.data.LabWork;
import common.exceptions.EmptyCollectionException;

public class MinByPersonalQualitiesMinimumCommand extends CommandImpl {
    private final CollectionManager<LabWork> collectionManager;

    public MinByPersonalQualitiesMinimumCommand(CollectionManager<LabWork> cm){
        super("min_by_personal_qualities_minimum", CommandType.NORMAL);
        collectionManager = cm;
    }
    @Override
    public String execute() throws InvalidDataException {
        if (collectionManager.getCollection().isEmpty()) throw new EmptyCollectionException();
        return  collectionManager.minByPersonalQualitiesMinimum();
    }
}
