package server.commands;

import common.collection.LabWorkManager;
import common.auth.User;
import common.commands.CommandImpl;
import common.commands.CommandType;
import common.connection.CollectionOperation;
import common.exceptions.AuthException;
import common.exceptions.EmptyCollectionException;
import common.exceptions.InvalidDataException;
import common.exceptions.PermissionException;

public class RemoveFirstCommand extends CommandImpl {
    private final LabWorkManager collectionManager;

    public RemoveFirstCommand(LabWorkManager cm) {
        super("remove_first", CommandType.NORMAL, CollectionOperation.REMOVE);
        collectionManager = cm;
    }


    @Override
    public String execute() throws InvalidDataException, AuthException {
        User user = getArgument().getUser();

        if (collectionManager.getCollection().isEmpty()) throw new EmptyCollectionException();
        int id = collectionManager.getCollection().iterator().next().getId();
        String owner = collectionManager.getByID(id).getUserLogin();
        String workerCreatorLogin = user.getLogin();
        if (workerCreatorLogin == null || !workerCreatorLogin.equals(owner))
            throw new PermissionException(owner);
        collectionManager.removeFirst();
        return "element #" + id + " successfully deleted";
    }

}
