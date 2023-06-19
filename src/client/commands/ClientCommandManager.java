package client.commands;


import static common.io.ConsoleOutputter.*;

import client.client.Client;
import common.exceptions.*;
import common.commands.*;
import common.connection.*;
import common.io.FileInputManager;

import java.io.File;
/**
 * command manager for client
 */
public class ClientCommandManager extends CommandManager {
    private final Client client;
    public ClientCommandManager(Client c) {
        client = c;
        addCommand(new ExecuteScriptCommand(this));
        addCommand(new ExitCommand());
        addCommand(new HelpCommand());
        addCommand(new PrintUniqueSalaryCommand(client.getWorkerManager()));
        addCommand(new GroupCountingByEndDateCommand(client));
        addCommand(new FilterStartsWithNameCommand(client.getWorkerManager()));
    }

    public Client getClient() {
        return client;
    }

    @Override

    public AnswerMsg runCommand(Request msg) {
        AnswerMsg res = new AnswerMsg();
        if (hasCommand(msg)) {
            res = (AnswerMsg) super.runCommand(msg);
            if(res.getStatus() == Response.Status.EXIT){
                res.info("shutting down...");
            }
        } else {
            //lock.lock();
            try {

                if(client.getUser()!=null && msg.getUser()==null) msg.setUser(client.getUser());
                else client.setAttemptUser(msg.getUser());
                client.send(msg);
                //if(msg.getUser()!=null)print(msg.getUser().getLogin());
               // while (!client.isReceivedRequest()) condition.await();
                //condition.signalAll();
                //res = (AnswerMsg) client.receive();
            } catch (ConnectionTimeoutException e) {
                res.info("no attempts left, shutting down").setStatus(Response.Status.EXIT);
            } catch (ConnectionException e) {
                res.error(e.getMessage());
            } finally {
                //lock.unlock();
            }
        }
        print(res);
        return res;
    }
    @Override
    public void fileMode(String path) throws FileException {
        currentScriptFileName=path;
        inputManager = new FileInputManager(path);
        isRunning = true;
        while (isRunning && inputManager.hasNextLine()) {
            CommandMsg commandMsg = inputManager.readCommand();
            Response answerMsg = runCommand(commandMsg);
            if (answerMsg.getStatus() == Response.Status.EXIT||answerMsg.getStatus() == Response.Status.ERROR) {
                close();
            }
        }
    }

    public void runFile(File file) throws FileException{
        currentScriptFileName=file.getName();
        inputManager = new FileInputManager(file);
        isRunning = true;
        while (isRunning && inputManager.hasNextLine()) {
            CommandMsg commandMsg = inputManager.readCommand();
            Response answerMsg = runCommand(commandMsg);
            if (answerMsg.getStatus() == Response.Status.EXIT||answerMsg.getStatus() == Response.Status.ERROR) {
                close();
            }
        }
    }
}
