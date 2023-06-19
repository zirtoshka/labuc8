package server.commands;

import common.collection.LabWorkManager;
import common.commands.CommandImpl;
import common.commands.CommandType;
import common.connection.AnswerMsg;
import common.connection.CollectionOperation;
import common.connection.Response;
import common.data.Worker;
import common.exceptions.CommandException;
import common.exceptions.InvalidDataException;

import java.util.Arrays;

public class AddCommand extends CommandImpl {
    private final LabWorkManager collectionManager;

    public AddCommand(LabWorkManager cm) {
        super("add", CommandType.NORMAL, CollectionOperation.ADD);
        collectionManager = cm;
    }

    @Override
    public Response run() throws InvalidDataException, CommandException {
        collectionManager.add(getWorkerArg());

        return new AnswerMsg().info( "Added element: " + getWorkerArg().toString()).setCollection(Arrays.asList(getWorkerArg()));
    }
}
