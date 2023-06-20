package server.commands;

import common.command.core.CommandImpl;
import common.command.core.CommandType;
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
