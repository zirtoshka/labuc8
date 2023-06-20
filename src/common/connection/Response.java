package common.connection;

import common.data.LabWork;

import java.io.Serializable;
import java.util.Collection;

public interface Response extends Serializable{
    public String getMessage();
    public Status getStatus();

    /**
     * Status enum:
     * ERROR: Not fatal error that user can fix
     * FINE: All right
     * EXIT: Need to stop program
     */
    public enum Status {
        ERROR,
        FINE,
        EXIT,
        CHECK_ID,
        AUTH_SUCCESS,
        BROADCAST,
        COLLECTION,
        LOGOUT
    }
    CollectionOperation getCollectionOperation();

    public Collection<LabWork> getCollection();
}