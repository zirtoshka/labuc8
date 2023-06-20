package server.commands;


import common.commands.CommandManager;
import common.commands.core.Command;
import common.commands.core.CommandType;
import common.data.LabWork;
import server.auth.UserManager;
import common.auth.User;
import common.connection.AnswerMsg;
import common.connection.Request;
import common.connection.Response;
import common.exceptions.AuthException;
import common.exceptions.CommandException;
import common.exceptions.ConnectionException;
import server.collection.LabWorkCollectionManager;
import server.log.Log;
import server.server.Server;

public class ServerCommandManager extends CommandManager {
    private final Server server;

    private final UserManager userManager;

    public ServerCommandManager(Server serv) {
        server = serv;
        LabWorkCollectionManager collectionManager = server.getCollectionManager();
        userManager = server.getUserManager();
        addCommand( new ExitCommand());
        addCommand( new HelpCommand());
        addCommand(new InfoCommand(collectionManager));
        addCommand(new AddCommand(collectionManager));
        addCommand(new AddIfMaxCommand(collectionManager));
        addCommand(new UpdateCommand(collectionManager));
        addCommand(new RemoveByIdCommand(collectionManager));
        addCommand(new ClearCommand(collectionManager));
        addCommand(new ShowCommand(collectionManager));
        addCommand(new FilterStartsWithNameCommand(collectionManager));

        addCommand(new LoginCommand(userManager));
        addCommand(new RegisterCommand(userManager));
        addCommand(new LogoutCommand(userManager));
        addCommand(new ShowUsersCommand(userManager));
    }

    public Server getServer() {
        return server;
    }

    @Override
    public Response runCommand(Request msg) {
        AnswerMsg res = new AnswerMsg();
        User user = msg.getUser();
        String cmdName = msg.getCommandName();
        boolean isGeneratedByServer = (msg.getStatus() != Request.Status.RECEIVED_BY_SERVER);
        try {
            Command cmd = getCommand(msg);
            //allow to execute a special command without authorization
            if (cmd.getType() != CommandType.AUTH && cmd.getType() != CommandType.SPECIAL) {
                if (isGeneratedByServer) {
                    user = server.getHostUser();
                    msg.setUser(user);
                }
                if (user == null) throw new AuthException();
                if (!userManager.isValid(user)) throw new AuthException();

                //link user to worker
                LabWork labWork = msg.getLabWork();
                if (labWork != null) labWork.setUser(user);
            }

            //executing command
            res = (AnswerMsg) super.runCommand(msg);
        } catch (ConnectionException | CommandException e) {
            res.error(e.getMessage());
        }
        String message = "";

        //format user and current command
        if (user != null) message += "[" + user.getLogin() + "] ";
        if (cmdName != null) message += "[" + cmdName + "] ";

        //format multiline output
        if (res.getMessage().contains("\n")) message += "\n";
        switch (res.getStatus()) {
            case EXIT:
                Log.logger.info(message + "shutting down...");
                server.close();
                break;
            case ERROR:
                Log.logger.error(message + res.getMessage());
                break;
            case AUTH_SUCCESS: //check if auth command was invoked by server terminal
                if (isGeneratedByServer) server.setHostUser(user);
            case CHECK_ID:
                if (msg.getLabWork() == null) ;
            default:
                Log.logger.info(message + res.getMessage());
                break;
        }
        return res;
    }
}
