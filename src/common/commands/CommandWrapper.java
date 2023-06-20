package common.commands;

/**
 * command wrapper class for command parsing
 */
public class CommandWrapper {
    private final String argument;
    private final String nameCommand;
    public CommandWrapper(String cmd, String arg){
        argument = arg;
        nameCommand = cmd;
    }
    public CommandWrapper(String cmd){
        argument = "default";
        nameCommand = cmd;
    }

    public String getCommand(){
        return nameCommand;
    }

    public String getArg(){
        return argument;
    }
}
