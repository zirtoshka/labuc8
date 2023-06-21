package common.commands;


import common.commands.core.Command;
import common.commands.core.Commandable;
import common.commands.core.Commands;
import common.connection.*;
import common.exceptions.*;
import common.io.ConsoleInputManager;
import common.io.FileInputManager;
import common.io.InputManager;
import server.exceptions.CheckIdException;

import java.io.Closeable;
import java.util.NoSuchElementException;
import java.util.Stack;

import static common.io.OutputManager.print;


public abstract class CommandManager implements Commandable, Closeable {
    private final Commands commands;
    private InputManager inputManager;
    private boolean isRunning;
    private String currentScriptFileName;
    private static final Stack<String> callStack = new Stack<>();

    public void clearStack(){
        callStack.clear();
    }
    public Stack<String> getStack(){
        return callStack;
    }
    public String getCurrentScriptFileName(){
        return currentScriptFileName;
    }
    public CommandManager(){
        isRunning = false;
        currentScriptFileName = "";
        commands = new Commands();
    }
    public void addCommand(Command c) {
        commands.addCommand(c.getName(),c);
    }
    public void addCommand(String key, Command c){
        commands.addCommand(key, c);
    }

    public Command getCommand(String s){
        System.out.println(s+"ddddddd");
        if (!hasCommand(s)) throw new NoSuchCommandException();
        Command cmd =  commands.get(s);
        return cmd;
    }
    public boolean hasCommand(String s){
        return commands.hasCommand(s);
    }

    public Response runCommand(Request msg) {
        AnswerMsg res = new AnswerMsg();
        try {
            Command cmd = getCommand(msg);
            cmd.setArgument(msg);
            res = (AnswerMsg) cmd.run();
        } catch (ExitException e) {
            res.setStatus(Response.Status.EXIT);
        } catch(CheckIdException e){
                res.setStatus(Response.Status.CHECK_ID);
        } catch (CommandException | CollectionException | InvalidDataException | FileException | ConnectionException e) {
            res.error(e.getMessage());
        }
        return res;
    }

    public void consoleMode() {
        inputManager = new ConsoleInputManager();
        isRunning = true;
        while (isRunning) {
            Response answerMsg = new AnswerMsg();
            print("enter command (help to get command list): ");
            try {
                CommandMsg commandMsg = inputManager.readCommand();
                if (commandMsg.getCommandName() != null)
                    if (commandMsg.getStringArg() != null)
                        commands.pushToHistory(commandMsg.getCommandName() + " " + commandMsg.getStringArg());
                    else
                        commands.pushToHistory(commandMsg.getCommandName());
                answerMsg = runCommand(commandMsg);
            } catch (NoSuchElementException e) {
                close();
                print("user input closed");
                break;
            }
            if (answerMsg.getStatus() == Response.Status.EXIT) {
                close();
            }
        }
    }
    public void fileMode(String path) throws FileException {
        currentScriptFileName = path;
        inputManager = new FileInputManager(path);
        isRunning = true;
        while (isRunning && inputManager.hasNextLine()) {
            CommandMsg commandMsg = inputManager.readCommand();
            Response answerMsg = runCommand(commandMsg);
            if (answerMsg.getStatus() == Response.Status.EXIT) {
                close();
            }
        }
    }

    public Stack<String> getCommandHistory(){
        return commands.getCommandHistory();
    }
    public void setInputManager(InputManager in){
        inputManager = in;
    }
    public InputManager getInputManager(){
        return inputManager;
    }
    public boolean isRunning(){
        return isRunning;
    }

    public void setRunning(boolean running){
        isRunning = running;
    }
    public void close(){
        setRunning(false);
    }
}
