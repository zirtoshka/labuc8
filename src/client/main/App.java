package client.main;


import java.util.ResourceBundle;

import client.client.Client;
import client.controllers.AskWindowController;
import client.controllers.LoginWindowController;
import client.controllers.MainWindowController;
import client.controllers.tools.ObservableResourceFactory;
import client.io.OutputterUI;
import common.exceptions.*;
import common.utils.DateConverter;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import static common.io.ConsoleOutputter.*;


public class App extends Application {
    //public static Logger logger = LogManager.getLogger("logger");
    //static final Logger logger = LogManager.getRootLogger();
    private static final String APP_TITLE = "LabWork Manager";
    public static final String BUNDLE = "resources.bundles.gui";
    private Stage primaryStage;
    static Client client;
    static String address;
    static int port;
    private static ObservableResourceFactory resourceFactory;
    private OutputterUI outputter;

    public static void main(String[] args) {
        resourceFactory = new ObservableResourceFactory();
        resourceFactory.setResources(ResourceBundle.getBundle(BUNDLE));
        //OutputerUI.setResourceFactory(resourceFactory);
        //Outputer.setResourceFactory(resourceFactory);


        if (initialize(args)) launch(args);
        else System.exit(0);
    }

    private static boolean initialize(String[] args) {
        address = "localhost";
        String strPort = "2023";
        port = 0;
        try {
            if (args.length == 2) {
                address = args[0];
                strPort = args[1];
            }
            if (args.length == 1) {
                strPort = args[0];
                print("no address passed by arguments, setting default : " + address);
            }
            if (args.length == 0) {
                print("no port and no address passed by arguments, setting default :" + address + "/" + strPort);
            }
            try {
                port = Integer.parseInt(strPort);
            } catch (NumberFormatException e) {
                throw new common.exceptions.InvalidPortException();
            }

        } catch (common.exceptions.ConnectionException e) {
            print(e.getMessage());
            return false;
        }
        return true;
    }

    public void initClient() {
        try {
            client = new Client(address, port);
            client.setOutputManager(outputter);
            client.setResourceFactory(resourceFactory);
            client.connectionTest();
        } catch (ConnectionException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void init() {
        resourceFactory = new ObservableResourceFactory();
        resourceFactory.setResources(ResourceBundle.getBundle(BUNDLE));
        outputter = new OutputterUI(resourceFactory);
        initClient();
    }

    @Override
    public void start(Stage stage) {
        primaryStage = stage;
        setLoginWindow();
    }

    public void setLoginWindow() {
        try {
            FXMLLoader loginWindowLoader = new FXMLLoader();
            loginWindowLoader.setLocation(getClass().getResource("/resources/view/LoginWindow.fxml"));
            Parent loginWindowRootNode = loginWindowLoader.load();
            Scene loginWindowScene = new Scene(loginWindowRootNode);
            LoginWindowController loginWindowController = loginWindowLoader.getController();
            loginWindowController.setApp(this);
            loginWindowController.setClient(client);
            loginWindowController.initLangs(resourceFactory);
            primaryStage.setWidth(600);
            primaryStage.setHeight(670);

            primaryStage.setTitle(APP_TITLE);

            primaryStage.setScene(loginWindowScene);
            primaryStage.setResizable(false);
            primaryStage.show();
        } catch (Exception exception) {
            // TODO: Обработать ошибки
            System.out.println(exception + " aaaa");
            exception.printStackTrace();
        }
    }

    public void setMainWindow() {
        try {
            FXMLLoader mainWindowLoader = new FXMLLoader();
            mainWindowLoader.setLocation(getClass().getResource("/resources/view/MainWindow.fxml"));
            Parent mainWindowRootNode = mainWindowLoader.load();
            Scene mainWindowScene = new Scene(mainWindowRootNode);
            MainWindowController mainWindowController = mainWindowLoader.getController();
//            mainWindowController.initLangs(resourceFactory);

            FXMLLoader askWindowLoader = new FXMLLoader();
            askWindowLoader.setLocation(getClass().getResource("/resources/view/AskWindow.fxml"));
            Parent askWindowRootNode = askWindowLoader.load();
            Scene askWindowScene = new Scene(askWindowRootNode);
            Stage askStage = new Stage();
            askStage.setTitle(APP_TITLE);
            askStage.setScene(askWindowScene);
            askStage.setResizable(false);
            askStage.initModality(Modality.WINDOW_MODAL);
            askStage.initOwner(primaryStage);
            AskWindowController askWindowController = askWindowLoader.getController();
            askWindowController.setAskStage(askStage);
            askWindowController.initLangs(resourceFactory);

            mainWindowController.setApp(this);
            mainWindowController.setUsername(client.getUser() != null ? client.getUser().getLogin() : "");
            mainWindowController.setClient(client);
            mainWindowController.setAskStage(askStage);
            mainWindowController.setPrimaryStage(primaryStage);
            mainWindowController.setAskWindowController(askWindowController);
            mainWindowController.initLangs(resourceFactory);


            mainWindowController.initFilter();
//            mainWindowController.setApp(this);


            primaryStage.setScene(mainWindowScene);
            primaryStage.setWidth(1100);
            primaryStage.setHeight(670);
            primaryStage.setResizable(true);
            primaryStage.setOnCloseRequest((e) -> {
                print("exiting...");
                client.close();
            });


        } catch (Exception exception) {
            // TODO: Обработать ошибки
            System.out.println(exception);
            exception.printStackTrace();
        }
    }

    public OutputterUI getOutputManager() {
        return outputter;
    }
}
