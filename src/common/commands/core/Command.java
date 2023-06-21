package common.commands.core;

import common.connection.CollectionOperation;
import common.connection.Request;
import common.connection.Response;
import common.exceptions.ConnectionException;
import common.exceptions.FileException;
import common.exceptions.InvalidDataException;

/**
 * Command callback interface
 */

public interface Command {
    public Response run() throws InvalidDataException, FileException, ConnectionException;
    public String getName();
    public CommandType getType();
    public CollectionOperation getCollectionOperation();
    public void setArgument(Request a);
}
