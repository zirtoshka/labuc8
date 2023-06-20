package server.commands;

import common.commands.core.CommandImpl;
import common.commands.core.CommandType;
import server.auth.UserManager;
import common.auth.User;
import common.connection.AnswerMsg;
import common.connection.Response;
import common.exceptions.AuthException;

public class LoginCommand extends CommandImpl {
    private final UserManager userManager;

    public LoginCommand(UserManager manager) {
        super("login", CommandType.AUTH);
        userManager = manager;
    }

    @Override
    public Response run() throws AuthException {
        User user = getArgument().getUser();
        if (user != null && user.getLogin() != null && user.getPassword() != null) {
            if (userManager.isValid(user)) {
                return new AnswerMsg().info("login successful").setStatus(Response.Status.AUTH_SUCCESS);
            }
        }
        throw new AuthException("login or password is incorrect");
    }
}
