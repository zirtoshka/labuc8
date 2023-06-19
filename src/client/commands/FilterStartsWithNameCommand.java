package client.commands;

import client.collection.LabWorkObservableManager;
import client.controllers.MainWindowController;
import common.commands.CommandImpl;
import common.commands.CommandType;
import common.data.Worker;
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
        List<Worker> list = collectionManager.filterStartsWithName(getStringArg());
        MainWindowController controller = collectionManager.getController();
        Platform.runLater(()->{
            controller.getFilter().filter(controller.getNameColumn(), "^" + getStringArg()+".*", Worker::getName);
        });
        if (list.isEmpty()) return "none of elements have name which starts with " + start;
        return list.stream()
                .sorted(new Worker.SortingComparator())
                .map(Worker::toString).reduce("", (a, b) -> a + b + "\n");
    }
}
