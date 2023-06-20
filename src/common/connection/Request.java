package common.connection;

import common.auth.User;
import common.data.LabWork;

import java.io.Serializable;

public interface Request extends Serializable {
    String getStringArg();

    LabWork getLabWork();

    String getCommandName();

    User getUser();

    Request setUser(User usr);

    Status getStatus();

    Request setStatus(Status s);

    enum Status {
        DEFAULT,
        HELLO,
        CONNECTION_TEST,
        EXIT,
        SENT_FROM_CLIENT,
        RECEIVED_BY_SERVER
    }


}
