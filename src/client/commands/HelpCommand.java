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
    help : show help for available commands\r
    \r
    info : Write to standard output information about the collection (type,\r
    initialization date, number of elements, etc.)\r
    \r
    show : print to standard output all elements of the collection in\r
    string representation\r
    \r
    add {element} : add a new element to the collection\r
    \r
    update id {element} : update the value of the collection element whose id\r
    equal to given\r
    \r
    remove_by_id id : remove an element from the collection by its id\r
    \r
    clear : clear the collection\r
    \r
    load (file_name - optional): load collection from file\r
    \r
    execute_script file_name : read and execute script from specified file.\r
    The script contains commands in the same form in which they are entered\r
    user is interactive.\r
    \\r
        exit : exit the program (without saving to a file)\\r
        \\r
        add_if_max {element} : add a new element to the collection if its\\r
        \\r
        value is greater than the value of the largest element of this collection\\r
        \\r
        remove_lower {element} : remove from the collection all elements smaller\\r
        than the given one\\r
        \\r
        history : print the last 10 commands (without their arguments)\\r
        \\r
        min_by_personal_qualities_minimum : print any object from the collection whose\\r
        personalQualitiesMinimum field value is the minimum\\r
        \\r
        max_by_discipline : print any object from the collection whose discipline field\\r
        value is the maximum\\r
        \\r
          filter_starts_with_name name : output elements, value of field name\\r
            which starts with the given substring\\r
            \\r
            login {element} : log in account by user name and password\\r
            \\r
            register {element} : register account by user name and password\\r
            \\r
            logout : log out from account\\r
            \\r
            show_users : print to standard output all users in string representation\\r
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
