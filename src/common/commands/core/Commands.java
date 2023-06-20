package common.commands.core;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class Commands {
    private final Map<String, Command> commands;
    private static final Stack<String> commandHistory = new Stack<>();

    public Commands(){
        commands = new HashMap<>();
    }

    public void addCommand(String key, Command c){
        commands.put(key, c);
    }

    public void pushToHistory(String key) {
        commandHistory.push(key);
    }

    public Command get(String key){
        return commands.get(key);
    }

    public boolean hasCommand(String s){
        return commands.containsKey(s);
    }

    public Stack<String> getCommandHistory(){
        return commandHistory;
    }
}
