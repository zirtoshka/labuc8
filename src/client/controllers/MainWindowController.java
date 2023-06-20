package client.controllers;


import client.client.Client;
import client.controllers.tools.ObservableResourceFactory;
import client.controllers.tools.TableFilter;
import client.controllers.tools.ZoomOperator;
import client.main.App;
import common.connection.CommandMsg;
import common.connection.Request;
import common.connection.Response;
import common.data.*;
import common.exceptions.ConnectionException;
import common.exceptions.ConnectionTimeoutException;
import common.exceptions.InvalidDataException;
import common.utils.DateConverter;
import javafx.animation.ScaleTransition;
import javafx.collections.transformation.SortedList;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.Serializable;
import java.net.URL;
import java.util.*;

/**
 * Main window controller.
 */
public class MainWindowController implements Initializable {
    public static final String LOGIN_COMMAND_NAME = "login";
    public static final String REGISTER_COMMAND_NAME = "register";
    public static final String REFRESH_COMMAND_NAME = "refresh";
    public static final String INFO_COMMAND_NAME = "info";
    public static final String ADD_COMMAND_NAME = "add";
    public static final String UPDATE_COMMAND_NAME = "update";
    public static final String REMOVE_COMMAND_NAME = "remove_by_id";
    public static final String CLEAR_COMMAND_NAME = "clear";
    public static final String EXIT_COMMAND_NAME = "exit";
    public static final String ADD_IF_MIN_COMMAND_NAME = "add_if_min";
    public static final String REMOVE_GREATER_COMMAND_NAME = "remove_greater";
    public static final String HISTORY_COMMAND_NAME = "history";
    public static final String SUM_OF_HEALTH_COMMAND_NAME = "sum_of_health";

    private final long RANDOM_SEED = 1821L;
    private final Duration ANIMATION_DURATION = Duration.millis(800);
    private final double MAX_SIZE = 100;


    @FXML
    private TableView<LabWork> labWorkTable;
    @FXML
    private TableColumn<LabWork, Integer> idColumn;
    @FXML
    private TableColumn<LabWork, String> ownerColumn;
    @FXML
    private TableColumn<LabWork, Date> creationDateColumn;
    @FXML
    private TableColumn<LabWork, String> nameColumn;

    @FXML
    private TableColumn<LabWork, Double> coordinatesXColumn;
    @FXML
    private TableColumn<LabWork, Integer> coordinatesYColumn;
    @FXML
    private TableColumn<LabWork, Integer> minimalPointColumn;

    @FXML
    private TableColumn<LabWork, Integer> personalQualitiesMinimumColumn;

    @FXML
    private TableColumn<LabWork, Double> averagePointColumn;

    @FXML
    private TableColumn<LabWork, Difficulty> difficultyColumn;
    @FXML
    private TableColumn<Discipline, String> nameDisciplineColumn;

    @FXML
    private TableColumn<Discipline, Integer> lectureHoursDisciplineColumn;
    @FXML
    private AnchorPane canvasPane;
    @FXML
    private Tab tableTab;
    @FXML
    private Tab canvasTab;
    @FXML
    private Button infoButton;
    @FXML
    private Button addButton;
    @FXML
    private Button updateButton;
    @FXML
    private Button removeButton;
    @FXML
    private Button clearButton;


    @FXML
    private Button refreshButton;
    @FXML
    private Button helpButton;
    @FXML
    private Tooltip infoButtonTooltip;
    @FXML
    private Tooltip addButtonTooltip;
    @FXML
    private Tooltip updateButtonTooltip;
    @FXML
    private Tooltip removeButtonTooltip;
    @FXML
    private Tooltip clearButtonTooltip;


    @FXML
    private Tooltip refreshButtonTooltip;
    @FXML
    private ComboBox<String> languageComboBox;
    @FXML
    private Label usernameLabel;

    private App app;
    private Tooltip shapeTooltip;
    private TableFilter<LabWork> tableFilter;
    private Client client;
    private Stage askStage;
    private Stage primaryStage;
    private FileChooser fileChooser;
    private AskWindowController askWindowController;
    private Map<String, Color> userColorMap;
    private Map<Shape, Integer> shapeMap;
    private Map<Integer, Text> textMap;
    private Shape prevClicked;
    private Color prevColor;
    private Random randomGenerator;
    private ObservableResourceFactory resourceFactory;
    private Map<String, Locale> localeMap;

    /**
     * Initialize main window.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeTable();
        initCanvas();
        fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File("."));
        userColorMap = new HashMap<>();
        shapeMap = new HashMap<>();
        textMap = new HashMap<>();
        randomGenerator = new Random(RANDOM_SEED);
        localeMap = new HashMap<>();
        localeMap.put("English", new Locale("en", "NZ"));
        localeMap.put("Русский", new Locale("ru", "RU"));
        localeMap.put("Deutsche", new Locale("de", "DE"));
        localeMap.put("Dansk", new Locale("da", "DK"));
        languageComboBox.setItems(FXCollections.observableArrayList(localeMap.keySet()));
    }

    /**
     * Initialize table.
     */
    private void initializeTable() {

        idColumn.setCellValueFactory(cellData ->
                new ReadOnlyObjectWrapper<>(cellData.getValue().getId()));
        ownerColumn.setCellValueFactory(cellData ->
                new ReadOnlyObjectWrapper<>(cellData.getValue().getUserLogin()));
        creationDateColumn.setCellValueFactory(cellData ->
                new ReadOnlyObjectWrapper<>(cellData.getValue().getCreationDate()));
        nameColumn.setCellValueFactory(cellData ->
                new ReadOnlyObjectWrapper<>(cellData.getValue().getName()));
        coordinatesXColumn.setCellValueFactory(cellData ->
                new ReadOnlyObjectWrapper<>(cellData.getValue().getCoordinates().getX()));
        coordinatesYColumn.setCellValueFactory(cellData ->
                new ReadOnlyObjectWrapper<>(cellData.getValue().getCoordinates().getY()));
        minimalPointColumn.setCellValueFactory(cellData ->
                new ReadOnlyObjectWrapper<>(cellData.getValue().getMinimalPoint()));
        personalQualitiesMinimumColumn.setCellValueFactory(cellData ->
                new ReadOnlyObjectWrapper<>(cellData.getValue().getPersonalQualitiesMinimum()));
        averagePointColumn.setCellValueFactory(cellData ->
                new ReadOnlyObjectWrapper<>(cellData.getValue().getAveragePoint()));
        difficultyColumn.setCellValueFactory(cellData ->
                new ReadOnlyObjectWrapper<>(cellData.getValue().getDifficulty()));
        nameDisciplineColumn.setCellValueFactory(cellData ->
                new ReadOnlyObjectWrapper<>(cellData.getValue().getName()));
        lectureHoursDisciplineColumn.setCellValueFactory(cellData ->
                new ReadOnlyObjectWrapper<>(cellData.getValue().getLectureHours()));

        creationDateColumn.setCellFactory(column -> {
            TableCell<LabWork, Date> cell = new TableCell<>() {
                @Override
                protected void updateItem(Date item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setText(null);
                    } else {
                        setText(DateConverter.dateToString(item));
                    }
                }
            };

            return cell;
        });



        //workerTable.setItems(FXCollections.observableArrayList());
        //s TableFilter<Worker> tableFilter = TableFilter.forTableView(workerTable).apply();
        /*workerTable.setOnSort((e)->{
            workerTable.getSortOrder().stream().sorted().collect();
        });*/
        //FilteredTableView y;

    }

    public void initFilter() {
        tableFilter = new TableFilter<LabWork>(labWorkTable, client.getLabWorkManager().getCollection(), resourceFactory)
                .addFilter(idColumn, (w) -> Integer.toString(w.getId()))
                .addFilter(nameColumn, (w) -> w.getName())
                .addFilter(coordinatesXColumn, (w) -> Double.toString(w.getCoordinates().getX()))
                .addFilter(coordinatesYColumn, (w) -> Integer.toString(w.getCoordinates().getY()))
                .addFilter(creationDateColumn, (w) -> DateConverter.dateToString(w.getCreationDate()))
                .addFilter(minimalPointColumn, (w) -> Integer.toString(w.getMinimalPoint()))
                .addFilter(personalQualitiesMinimumColumn, (w) -> Integer.toString(w.getPersonalQualitiesMinimum()))

                .addFilter(minimalPointColumn, (w) -> Integer.toString(w.getMinimalPoint()))

                .addFilter(ownerColumn, (w) -> w.getUserLogin());
    }

    public TableFilter<LabWork> getFilter() {
        return tableFilter;
    }

    public TableColumn<LabWork, ?> getNameColumn() {
        return nameColumn;
    }

    private void initCanvas() {
        ZoomOperator zoomOperator = new ZoomOperator();

// Listen to scroll events (similarly you could listen to a button click, slider, ...)
        canvasPane.setOnScroll(new EventHandler<ScrollEvent>() {
            @Override
            public void handle(ScrollEvent event) {
                double zoomFactor = 1.5;
                if (event.getDeltaY() <= 0) {
                    // zoom out
                    zoomFactor = 1 / zoomFactor;

                }

                double x = event.getSceneX();
                double y = event.getSceneY();

                //if(!(event.getDeltaY()<=0 && (zoomOperator.getBounds().getHeight()<=1000||zoomOperator.getBounds().getWidth()<=1000))){
                /*if((event.getDeltaY()<=0 && (zoomOperator.getBounds().getMinX()>=1000||
                        zoomOperator.getBounds().getMinY()>=1000||
                        zoomOperator.getBounds().getMaxX()<=2000-1000||
                        zoomOperator.getBounds().getMaxY()<=2000-1000))) return;*/
                if ((event.getDeltaY() <= 0 && (zoomOperator.getBounds().getHeight() <= 500 || zoomOperator.getBounds().getWidth() <= 500)))
                    return;
                zoomOperator.zoom(canvasPane, zoomFactor, x, y);


            }
        });
        /*canvasPane.setOnMouseDragged(event -> {
            canvasPane.setManaged(false);
            canvasPane.setTranslateX(event.getX() + canvasPane.getTranslateX());
            canvasPane.setTranslateY(event.getY() + canvasPane.getTranslateY());
            event.consume();
        });*/

        zoomOperator.draggable(canvasPane);
        canvasPane.setMinWidth(2000);
        canvasPane.setMinHeight(2000);
    }

    /**
     * Bind gui language.
     */
    private void bindGuiLanguage() {
        resourceFactory.setResources(ResourceBundle.getBundle
                (App.BUNDLE, localeMap.get(languageComboBox.getSelectionModel().getSelectedItem())));

        idColumn.textProperty().bind(resourceFactory.getStringBinding("IdColumn"));
        ownerColumn.textProperty().bind(resourceFactory.getStringBinding("OwnerColumn"));
        creationDateColumn.textProperty().bind(resourceFactory.getStringBinding("CreationDateColumn"));
        nameColumn.textProperty().bind(resourceFactory.getStringBinding("NameColumn"));
        coordinatesXColumn.textProperty().bind(resourceFactory.getStringBinding("CoordinatesXColumn"));
        coordinatesYColumn.textProperty().bind(resourceFactory.getStringBinding("CoordinatesYColumn"));
        minimalPointColumn.textProperty().bind(resourceFactory.getStringBinding("MinimalPointColumn"));
        personalQualitiesMinimumColumn.textProperty().bind(resourceFactory.getStringBinding("PersonalQualitiesMinimumColumn"));
        averagePointColumn.textProperty().bind(resourceFactory.getStringBinding("AveragePointColumn"));
        difficultyColumn.textProperty().bind(resourceFactory.getStringBinding("DifficultyColumn"));
        nameDisciplineColumn.textProperty().bind(resourceFactory.getStringBinding("NameDisciplineColumn"));
        lectureHoursDisciplineColumn.textProperty().bind(resourceFactory.getStringBinding("LectureHoursDisciplineColumn"));


        tableTab.textProperty().bind(resourceFactory.getStringBinding("TableTab"));
        canvasTab.textProperty().bind(resourceFactory.getStringBinding("CanvasTab"));

        infoButton.textProperty().bind(resourceFactory.getStringBinding("InfoButton"));
        addButton.textProperty().bind(resourceFactory.getStringBinding("AddButton"));
        updateButton.textProperty().bind(resourceFactory.getStringBinding("UpdateButton"));
        removeButton.textProperty().bind(resourceFactory.getStringBinding("RemoveButton"));
        clearButton.textProperty().bind(resourceFactory.getStringBinding("ClearButton"));
        refreshButton.textProperty().bind(resourceFactory.getStringBinding("RefreshButton"));
        helpButton.textProperty().bind(resourceFactory.getStringBinding("HelpButton"));

        infoButtonTooltip.textProperty().bind(resourceFactory.getStringBinding("InfoButtonTooltip"));
        addButtonTooltip.textProperty().bind(resourceFactory.getStringBinding("AddButtonTooltip"));
        updateButtonTooltip.textProperty().bind(resourceFactory.getStringBinding("UpdateButtonTooltip"));
        removeButtonTooltip.textProperty().bind(resourceFactory.getStringBinding("RemoveButtonTooltip"));
        clearButtonTooltip.textProperty().bind(resourceFactory.getStringBinding("ClearButtonTooltip"));
        refreshButtonTooltip.textProperty().bind(resourceFactory.getStringBinding("RefreshButtonTooltip"));
    }

    /**
     * Refresh button on action.
     */
    @FXML
    public void refreshButtonOnAction() {
        requestAction(REFRESH_COMMAND_NAME);
    }

    /**
     * Info button on action.
     */
    @FXML
    private void infoButtonOnAction() {
        client.getCommandManager().runCommand(new CommandMsg("info"));
    }


    /**
     * Update button on action.
     */
    @FXML
    private void updateButtonOnAction() {
       /* Worker worker = null;
        try {
            worker = new DefaultWorker("a",new Coordinates(1,2L),1000L, DateConverter.parseLocalDate("2000-01-01"),
                    Position.BAKER,Status.PROBATION,new Organization("XXX",OrganizationType.GOVERNMENT));
        } catch (InvalidDateFormatException e) {
            e.printStackTrace();
        }
        workerTable.getItems().add(worker);
*/
        LabWork labWork = labWorkTable.getSelectionModel().getSelectedItem();
        // int idx = workerTable.getSelectionModel().getSelectedIndex() + 1;
        //int i = workerTable.getSelectionModel().getSelectedIndex();
        if (labWork != null) {
            askWindowController.setLabWork(labWork);
            try {
                client.getCommandManager().runCommand(new CommandMsg("update").setArgument(Integer.toString(labWork.getId())).setLabWork(askWindowController.readLabWork()));
            } catch (InvalidDataException e) {
                //e.printStackTrace();
            }
        }


        /*if (!spaceMarineTable.getSelectionModel().isEmpty()) {
            long id = spaceMarineTable.getSelectionModel().getSelectedItem().getId();
            askWindowController.setMarine(spaceMarineTable.getSelectionModel().getSelectedItem());
            askStage.showAndWait();
            MarineRaw marineRaw = askWindowController.getAndClear();
            if (marineRaw != null) requestAction(UPDATE_COMMAND_NAME, id + "", marineRaw);
        } else OutputterUI.error("UpdateButtonSelectionException");*/

    }

    /**
     * Remove button on action.
     */
    @FXML
    private void removeButtonOnAction() {
        LabWork labWork = labWorkTable.getSelectionModel().getSelectedItem();
        if (labWork != null)
            client.getCommandManager().runCommand(new CommandMsg("remove_by_id").setArgument(Integer.toString(labWork.getId())));
        /*if (!spaceMarineTable.getSelectionModel().isEmpty())
            requestAction(REMOVE_COMMAND_NAME,
                    spaceMarineTable.getSelectionModel().getSelectedItem().getId().toString(), null);
        else OutputterUI.error("RemoveButtonSelectionException");*/
    }

    /**
     * Clear button on action.
     */
    @FXML
    private void clearButtonOnAction() {
        client.getCommandManager().runCommand(new CommandMsg("clear"));
    }


    /**
     * Add button on action.
     */
    @FXML
    private void addButtonOnAction() {
        //askWindowController.clearMarine();

        try {
            client.getCommandManager().runCommand(new CommandMsg("add").setLabWork(askWindowController.readLabWork()));
        } catch (InvalidDataException e) {

        }
        /*workerTable.refresh();
        refreshCanvas();*/
        //if (marineRaw != null) requestAction(ADD_COMMAND_NAME, "", marineRaw);*/
    }


    @FXML
    private void helpButtonOnAction() {
        client.getCommandManager().runCommand(new CommandMsg("help"));
    }

    /**
     * Request action.
     */
    private void requestAction(String commandName, String commandStringArgument, Serializable commandObjectArgument) {
        /*NavigableSet<SpaceMarine> responsedMarines = client.processRequestToServer(commandName, commandStringArgument,
                commandObjectArgument);
        if (responsedMarines != null) {
            ObservableList<SpaceMarine> marinesList = FXCollections.observableArrayList(responsedMarines);
            spaceMarineTable.setItems(marinesList);
            TableFilter.forTableView(spaceMarineTable).apply();
            spaceMarineTable.getSelectionModel().clearSelection();
            refreshCanvas();
        }*/
    }

    private boolean askServer(Request request) {
        Response response;
        try {
            client.send(request);
            response = client.receive();
        } catch (ConnectionTimeoutException e) {
            app.getOutputManager().error("ConnectionTimeoutException");
        } catch (InvalidDataException | ConnectionException e) {
            app.getOutputManager().error("ConnectionException");
        }
        //if(response!=null)
        return false;
    }

    /**
     * Binds request action.
     */
    private void requestAction(String commandName) {
        requestAction(commandName, "", null);
    }


    public void refreshTable() {
        labWorkTable.refresh();
        tableFilter.updateFilters();
    }

    /**
     * Refreshes canvas.
     */

    public void refreshCanvas() {
        shapeMap.keySet().forEach(s -> canvasPane.getChildren().remove(s));
        shapeMap.clear();
        textMap.values().forEach(s -> canvasPane.getChildren().remove(s));
        textMap.clear();
        SortedList<LabWork> list = labWorkTable.getItems().sorted((w1, w2) -> w1.getName().compareTo(w2.getName()) > 0 ? 0 : 1);
        for (LabWork labWork : list) {
            if (!userColorMap.containsKey(labWork.getUserLogin()))
                userColorMap.put(labWork.getUserLogin(),
                        Color.color(randomGenerator.nextDouble(), randomGenerator.nextDouble(), randomGenerator.nextDouble()));

            double size = MAX_SIZE;

            Shape circleObject = new Circle(size, userColorMap.get(labWork.getUserLogin()));
            circleObject.setOnMouseClicked(this::shapeOnMouseClicked);
            circleObject.translateXProperty().bind(canvasPane.widthProperty().divide(2).add(labWork.getCoordinates().getX()));
            circleObject.translateYProperty().bind(canvasPane.heightProperty().divide(2).subtract(labWork.getCoordinates().getY()));

            circleObject.setOpacity(0.5);

            Text textObject = new Text(Integer.toString(labWork.getId()));
            textObject.setOnMouseClicked(circleObject::fireEvent);
            textObject.setFont(Font.font(size / 3));
            textObject.setFill(userColorMap.get(labWork.getUserLogin()).darker());
            textObject.translateXProperty().bind(circleObject.translateXProperty().subtract(textObject.getLayoutBounds().getWidth() / 2));
            textObject.translateYProperty().bind(circleObject.translateYProperty().add(textObject.getLayoutBounds().getHeight() / 4));

            canvasPane.getChildren().add(circleObject);
            canvasPane.getChildren().add(textObject);
            shapeMap.put(circleObject, labWork.getId());
            textMap.put(labWork.getId(), textObject);

            ScaleTransition circleAnimation = new ScaleTransition(ANIMATION_DURATION, circleObject);
            ScaleTransition textAnimation = new ScaleTransition(ANIMATION_DURATION, textObject);
            circleAnimation.setFromX(0);
            circleAnimation.setToX(1);
            circleAnimation.setFromY(0);
            circleAnimation.setToY(1);
            textAnimation.setFromX(0);
            textAnimation.setToX(1);
            textAnimation.setFromY(0);
            textAnimation.setToY(1);
            circleAnimation.play();
            textAnimation.play();
        }

    }

    /**
     * Shape on mouse clicked.
     */
    private void shapeOnMouseClicked(MouseEvent event) {
        Shape shape = (Shape) event.getSource();
        //Tooltip.install(shape,shapeTooltip);
        long id = shapeMap.get(shape);
        for (LabWork worker : labWorkTable.getItems()) {
            if (worker.getId() == id) {
                if (shapeTooltip != null && shapeTooltip.isShowing()) shapeTooltip.hide();
                shapeTooltip = new Tooltip(worker.toString());
                shapeTooltip.setAutoHide(true);
                shapeTooltip.show(shape, event.getScreenX(), event.getScreenY());
                labWorkTable.getSelectionModel().select(worker);
                //shapeTooltip.setText(worker.toString());
                //shapeTooltip.show(primaryStage);
                break;
            }
        }
        if (prevClicked != null) {
            prevClicked.setFill(prevColor);
        }
        prevClicked = shape;
        prevColor = (Color) shape.getFill();
        shape.setFill(prevColor.brighter());
    }

    public void setClient(Client client) {
        this.client = client;
        labWorkTable.setItems(client.getLabWorkManager().getCollection());
        client.getLabWorkManager().setController(this);
        client.setResourceFactory(resourceFactory);
    }

    public void setUsername(String username) {
        usernameLabel.setText(username);
    }

    public void setAskStage(Stage askStage) {
        this.askStage = askStage;
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void setAskWindowController(AskWindowController askWindowController) {
        this.askWindowController = askWindowController;
    }

    public void initLangs(ObservableResourceFactory resourceFactory) {
        this.resourceFactory = resourceFactory;
        for (String localeName : localeMap.keySet()) {
            if (localeMap.get(localeName).equals(resourceFactory.getResources().getLocale()))
                languageComboBox.getSelectionModel().select(localeName);
        }
        if (languageComboBox.getSelectionModel().getSelectedItem().isEmpty())
            languageComboBox.getSelectionModel().selectFirst();
        languageComboBox.setOnAction((event) -> {
            Locale locale = localeMap.get(languageComboBox.getValue());
            resourceFactory.setResources(ResourceBundle.getBundle
                    (App.BUNDLE, locale));
            switch (locale.toString()) {
                case "en_NZ":
                    DateConverter.setPattern("yyyy-MM-dd");
                    break;
                case "ru_RU":
                    DateConverter.setPattern("yyyy/MM/dd");
                    break;
            }

            System.out.println(locale);
            labWorkTable.refresh();
        });
        bindGuiLanguage();
    }

    public void setApp(App a) {
        app = a;
    }


}
