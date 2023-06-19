package client.controllers;

import client.controllers.tools.ObservableResourceFactory;
import client.main.App;
import common.data.*;
import common.exceptions.*;
import common.utils.DateConverter;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


import javax.swing.text.Position;
import java.time.LocalDate;

public class AskWindowController {
    @FXML
    private Label nameLabel;
    @FXML
    private Label coordinatesXLabel;
    @FXML
    private Label coordinatesYLabel;

    @FXML
    private Label minimalPointLabel;
    @FXML
    private Label personalQualitiesMinimumLabel;
    @FXML
    private Label difficultyLabel;
    @FXML
    private Label averagePointLabel;

    @FXML
    private Label nameDisciplineLabel;
    @FXML
    private Label lectureHoursDisciplineLabel;



    @FXML
    private TextField nameField;
    @FXML
    private TextField coordinatesXField;
    @FXML
    private TextField coordinatesYField;
    @FXML
    private TextField minimalPointField;
    @FXML
    private TextField personalQualitiesMinimumField;
    @FXML
    private TextField nameDisciplineField;
    @FXML
    private TextField averagePointField;

    @FXML
    private TextField lectureHoursDisciplineField;
    @FXML
    private ComboBox<Difficulty> difficultyBox;


    @FXML
    private Button enterButton;

    private Stage askStage;
    private LabWork resultLabWork;
    private ObservableResourceFactory resourceFactory;
    private LabWork labWork;
    private App app;

    @FXML
    public void initialize() {
        askStage = new Stage();
        difficultyBox.setItems(FXCollections.observableArrayList(Difficulty.values()));
    }

    public String readName() throws InvalidDataException {
        String s = nameField.getText();
        if (s == null || s.equals("")) {
            throw new InvalidDataException("[NameEmptyException]");
        }
        return s;
    }



    public double readXCoord() throws InvalidDataException {
        double x;
        try {
            x = Float.parseFloat(coordinatesXField.getText());
        } catch (NumberFormatException e) {
            throw new InvalidDataException("[XCoordFormatException]");
        }
        if (Double.isInfinite(x) || Double.isNaN(x)) throw new InvalidDataException("[XCoordFormatException]");
        return x;
    }

    public Integer readYCoord() throws InvalidDataException {
        Integer y;
        try {
            y = Integer.parseInt(coordinatesYField.getText());
        } catch (NumberFormatException e) {
            throw new InvalidDataException("[YCoordFormatException]");
        }
        if (y <= -545) throw new InvalidDataException("[YCoordLimitException]");
        return y;
    }

    public Coordinates readCoords() throws InvalidDataException {
        double x = readXCoord();
        Integer y = readYCoord();
        Coordinates coord = new Coordinates(x, y);
        return coord;
    }

    public Integer readMinimalPoint() throws InvalidNumberException {
        Integer minimalPoint;
        try{
            minimalPoint = Integer.parseInt(minimalPointField.getText());
            if (minimalPoint <= 0) throw new InvalidNumberException();
        }
        catch(NumberFormatException e){
            throw new InvalidNumberException();
        }
        return minimalPoint;
    }

    public int readPersonalQualitiesMinimum() throws InvalidNumberException {
        int personalQualitiesMinimum;
        try{
            personalQualitiesMinimum = Integer.parseInt(personalQualitiesMinimumField.getText());
            if (personalQualitiesMinimum <= 0) throw new InvalidNumberException();
        }
        catch(NumberFormatException e){
            throw new InvalidNumberException();
        }
        return personalQualitiesMinimum;
    }

    public Double readAveragePoint() throws InvalidNumberException {
        Double averagePoint;
        try{
            averagePoint = Double.parseDouble(averagePointField.getText());
            if (averagePoint != null && (averagePoint <= 0)) throw new InvalidNumberException();
        }
        catch(NumberFormatException e){
            throw new InvalidNumberException();
        }
        return averagePoint;
    }

    public Difficulty readDifficulty() {
        return  difficultyBox.getSelectionModel().getSelectedItem();
    }
    public Integer readLectureHours() throws InvalidNumberException {
        Integer lectureHours;
        try{
            lectureHours = Integer.parseInt(lectureHoursDisciplineField.getText());
        }
        catch(NumberFormatException e){
            throw new InvalidNumberException();
        }
        return lectureHours;
    }
    public Discipline readDiscipline() throws InvalidDataException{
        String name = readName();
        Integer lectureHours = readLectureHours();
        return new Discipline(name, lectureHours);
    }




    public LabWork readLabWork() throws InvalidDataException {
        askStage.showAndWait();

        if (labWork == null) throw new InvalidDataException("");
        return labWork;

    }

    public void setLabWork(LabWork labWork) {
        nameField.setText(labWork.getName());
        coordinatesXField.setText(labWork.getCoordinates().getX() + "");
        coordinatesYField.setText(labWork.getCoordinates().getY() + "");
        salaryField.setText(labWork.getDifficulty() + "");
        organizationNameField.setText(worker.getOrganization().getFullName() != null ? worker.getOrganization().getFullName() : "");
        endDateField.setText(worker.getEndDate() != null ? DateConverter.dateToString(worker.getEndDate()) : "");
        difficultyBox.setValue(labWork.getDifficulty());
    }

    @FXML
    private void enterButtonOnAction() {
        try {

            String name = readName();
            Coordinates coords = readCoords();
            long salary = readDifficulty();
            LocalDate date = readEndDate();
            Position pos = readDifficulty();
            Status stat = readStatus();
            Organization org = readOrganization();
            worker = new DefaultWorker(name, coords, salary, date, pos, stat, org);

            askStage.close();
        } catch (InvalidDataException | IllegalArgumentException exception) {
            app.getOutputManager().error(exception.getMessage());
        }
    }
/*
    public String readPassword() throws InvalidDataException {
        String s = read();
        if (s.equals("")) throw new EmptyStringException();
        return s;
    }

    public String readLogin() throws InvalidDataException {
        String s = read();
        if (s.equals("")) throw new EmptyStringException();
        return s;
    }

    public User readUser() throws InvalidDataException {
        return new User(readPassword(), readLogin());
    }

    public CommandMsg readCommand() {
        String cmd = read();
        String arg = null;
        Worker worker = null;
        User user = null;
        if (cmd.contains(" ")) { //if command has argument
            String[] arr = cmd.split(" ", 2);
            cmd = arr[0];
            arg = arr[1];
        }
        if (cmd.equals("add") || cmd.equals("add_if_min") || cmd.equals("add_if_max") || cmd.equals("update")) {
            try {
                worker = readWorker();
            } catch (InvalidDataException ignored) {
            }
        } else if (cmd.equals("login") || cmd.equals("register")) {
            try {
                user = readUser();
            } catch (InvalidDataException ignored) {
            }
            return new CommandMsg(cmd, null, null, user);
        }
        return new CommandMsg(cmd, arg, worker);
    }*/

    public void setAskStage(Stage askStage) {
        this.askStage = askStage;
        this.askStage.setOnCloseRequest((e) -> worker = null);
    }

    public void setApp(App app) {
        this.app = app;
    }

    /**
     * Init langs.
     *
     * @param resourceFactory Resource factory to set.
     */
    public void initLangs(ObservableResourceFactory resourceFactory) {
        this.resourceFactory = resourceFactory;
        bindGuiLanguage();
    }

    public void bindGuiLanguage() {
        nameLabel.textProperty().bind(resourceFactory.getStringBinding("NameColumn"));
        coordinatesXLabel.textProperty().bind(resourceFactory.getStringBinding("CoordinatesXColumn"));
        coordinatesYLabel.textProperty().bind(resourceFactory.getStringBinding("CoordinatesYColumn"));
        endDateLabel.textProperty().bind(resourceFactory.getStringBinding("EndDateColumn"));
        positionLabel.textProperty().bind(resourceFactory.getStringBinding("PositionColumn"));
        statusLabel.textProperty().bind(resourceFactory.getStringBinding("StatusColumn"));
        organizationNameLabel.textProperty().bind(resourceFactory.getStringBinding("OrganizationNameColumn"));
        organizationTypeLabel.textProperty().bind(resourceFactory.getStringBinding("OrganizationTypeColumn"));
        salaryLabel.textProperty().bind(resourceFactory.getStringBinding("SalaryColumn"));
        enterButton.textProperty().bind(resourceFactory.getStringBinding("EnterButton"));
    }
}
