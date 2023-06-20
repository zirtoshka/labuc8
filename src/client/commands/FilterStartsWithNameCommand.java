package client.commands;

import client.collection.LabWorkObservableManager;
import client.controllers.MainWindowController;

import common.commands.core.CommandImpl;
import common.commands.core.CommandType;
import common.data.LabWork;
import common.exceptions.MissedCommandArgumentException;
import javafx.application.Platform;

import java.util.List;

public class FilterStartsWithNameCommand extends CommandImpl {
    private final LabWorkObservableManager collectionManager;

    public FilterStartsWithNameCommand(LabWorkObservableManager cm) {
        super("filter_starts_with_name", CommandType.NORMAL);
        collectionManager = cm;
    }

    @Override
    public String execute() {
        if (!hasStringArg()) throw new MissedCommandArgumentException();
        String start = getStringArg();
        List<LabWork> list = collectionManager.filterStartsWithName(getStringArg());
        MainWindowController controller = collectionManager.getController();
        Platform.runLater(()->{
            controller.getFilter().filter(controller.getNameColumn(), "^" + getStringArg()+".*", LabWork::getName);
        });
        if (list.isEmpty()) return "none of elements have name which starts with " + start;
        return list.stream()
                .sorted(new LabWork.SortingComparator())
                .map(LabWork::toString).reduce("", (a, b) -> a + b + "\n");
    }
}
