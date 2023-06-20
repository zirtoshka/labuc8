package server.commands;

import java.util.List;


import common.commands.core.CommandImpl;
import common.commands.core.CommandType;
import common.data.*;
import common.exceptions.*;
import server.collection.CollectionManager;

public class FilterStartsWithNameCommand extends CommandImpl {
    private CollectionManager<LabWork> collectionManager;
    public FilterStartsWithNameCommand(CollectionManager<LabWork> cm){
        super("filter_starts_with_name", CommandType.NORMAL);
        collectionManager = cm;
    }

    @Override
    public String execute(){
        if(!hasStringArg()) throw new MissedCommandArgumentException();
        if (collectionManager.getCollection().isEmpty()) throw new EmptyCollectionException();
        String start = getStringArg();
        List<LabWork> list = collectionManager.filterStartsWithName(getStringArg());
        if(list.isEmpty()) return "none of elements have name which starts with " + start;
        String s = list.stream()
                .sorted(new LabWork.SortingComparator())
                .map(e -> e.toString()).reduce("", (a,b)->a + b + "\n");
        return s;
    }
}
