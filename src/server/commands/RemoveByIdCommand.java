package server.commands;

import common.auth.User;

import common.commands.core.CommandImpl;
import common.commands.core.CommandType;
import common.exceptions.*;
import common.data.*;
import server.collection.CollectionManager;

import static common.utils.Parser.*;
public class RemoveByIdCommand extends CommandImpl {
    private CollectionManager<LabWork> collectionManager;
    public RemoveByIdCommand(CollectionManager<LabWork> cm){
        super("remove_by_id", CommandType.NORMAL);
        collectionManager = cm;
    }


    @Override
    public String execute() throws InvalidDataException, AuthException {
        User user = getArgument().getUser();
        if (collectionManager.getCollection().isEmpty()) throw new EmptyCollectionException();
        if (!hasStringArg()) throw new MissedCommandArgumentException();
        Integer id = parseId(getStringArg());
        if (!collectionManager.checkId(id))
            throw new InvalidCommandArgumentException("no such id #" + id);

        String owner = collectionManager.getById(id).getUserLogin();
        String labWorkCreatorLogin = user.getLogin();

        if (labWorkCreatorLogin == null || !labWorkCreatorLogin.equals(owner))
            throw new AuthException("you dont have permission, element was created by " + owner);
        collectionManager.removeById(id);
        return "element #" + id + " removed";
    }
}
