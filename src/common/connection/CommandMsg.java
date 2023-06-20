package common.connection;

import common.auth.User;
import common.data.LabWork;

/**
 * Message witch include command and arguments
 */
public class CommandMsg implements Request {
    private final String commandName;
    private String commandStringArgument;
    private LabWork labWork;
    private User user;
    private Status status;

    public CommandMsg(String commandNm, String commandSA, LabWork labWork) {
        commandName = commandNm;
        commandStringArgument = commandSA;
        labWork = labWork;
        user = null;
        status = Status.DEFAULT;
    }
    public  CommandMsg(){
        commandName = null;
        commandStringArgument=null;
        labWork = null;
        status = Status.DEFAULT;
    }
    public  CommandMsg(String s){
        commandName = s;
        commandStringArgument=null;
        labWork = null;
        status = Status.DEFAULT;
    }

    public CommandMsg(String commandNm, String commandSA, LabWork labWork, User usr) {
        commandName = commandNm;
        commandStringArgument = commandSA;
        labWork = labWork;
        user = usr;
        status = Status.DEFAULT;
    }

    public CommandMsg setStatus(Status s) {
        status = s;
        return this;
    }

    public Status getStatus() {
        return status;
    }

    public CommandMsg setUser(User usr) {
        user = usr;
        return this;
    }

    public CommandMsg setLabWork(LabWork labWork){
        this.labWork = labWork;
        return this;
    }

    public CommandMsg setArgument(String s){
        commandStringArgument = s;
        return this;
    }


    /**
     * @return Command name.
     */
    public String getCommandName() {
        return commandName;
    }

    /**
     * @return Command string argument.
     */
    public String getStringArg() {
        return commandStringArgument;
    }

    /**
     * @return Command object argument.
     */
    public LabWork getLabWork() {
        return labWork;
    }

    public User getUser() {
        return user;
    }
}