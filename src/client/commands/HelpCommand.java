package client.commands;

import common.commands.*;
import common.commands.core.CommandImpl;
import common.commands.core.CommandType;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

public class HelpCommand extends CommandImpl {
    private static final String TEXT_HELP = """
    \r
    help : show help for available options\r
    \r
    info : print information about the collection (type, initialization date, number of elements, etc.)\r
    \r
    add {element} : add a new element to the collection\r
    \r
    update id {element} : update the value of the collection element whose chosen\r
    \r
    remove id : remove an element from the collection whose chosen\r
    \r
    clear : clear the collection whose owner is you\r
    \r
    logout : log out from account\r
    \r
    """;

    public HelpCommand() {
        super("help", CommandType.NORMAL);
    }

    @Override
    public String execute() {
        /*Platform.runLater(()->{
            Text text = new Text(CommandManager.getHelp());
            TextFlow textFlow = new TextFlow(text);
            Stage stage = new Stage();
            Scene scene = new Scene(textFlow);
            stage.setScene(scene);
            stage.setTitle("s");
            stage.showAndWait();
        });*/
        return TEXT_HELP;
    }
}
