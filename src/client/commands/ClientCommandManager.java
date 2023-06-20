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
        addCommand(new ExitCommand());
        addCommand(new HelpCommand());
        addCommand(new FilterStartsWithNameCommand(client.getLabWorkManager()));
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


}
