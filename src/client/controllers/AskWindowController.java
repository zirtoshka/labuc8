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


import javax.print.DocFlavor;
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
        minimalPointField.setText(labWork.getMinimalPoint()+"");
        personalQualitiesMinimumField.setText(labWork.getPersonalQualitiesMinimum()+"");
        averagePointField.setText(labWork.getAveragePoint()+"");
        difficultyBox.setValue(labWork.getDifficulty());
        nameDisciplineField.setText(labWork.getDiscipline().getName()+"");
        lectureHoursDisciplineField.setText(labWork.getDiscipline().getLectureHours()+"");
    }

    @FXML
    private void enterButtonOnAction() {
        try {
            resultLabWork = new LabWork(
                    readName(),
                    readCoords(),
                    readMinimalPoint(),
                    readPersonalQualitiesMinimum(),
                    readAveragePoint(),
                    difficultyBox.getValue(),
                    readDiscipline()
            );
            askStage.close();
        } catch (InvalidDataException | IllegalArgumentException exception) {
            app.getOutputManager().error(exception.getMessage());
        }
    }
    public void clearLabWork() {
        nameField.clear();
        coordinatesXField.clear();
        coordinatesYField.clear();
        minimalPointField.clear();
        personalQualitiesMinimumField.clear();
        averagePointField.clear();
        difficultyBox.setValue(Difficulty.HOPELESS);
        nameDisciplineField.clear();
        lectureHoursDisciplineField.clear();

    }
    public LabWork getAndClear() {
        LabWork labWorkToReturn = resultLabWork;
        resultLabWork = null;
        return labWorkToReturn;
    }

    public void setAskStage(Stage askStage) {
        this.askStage = askStage;

    }

//    public void setApp(App app) {
//        this.app = app;
//    }

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
        minimalPointLabel.textProperty().bind(resourceFactory.getStringBinding("MinimalPointColumn"));
        personalQualitiesMinimumLabel.textProperty().bind(resourceFactory.getStringBinding("PersonalQualitiesMinimumColumn"));
        averagePointLabel.textProperty().bind(resourceFactory.getStringBinding("AveragePointColumn"));
        difficultyLabel.textProperty().bind(resourceFactory.getStringBinding("DifficultyColumn"));
        nameDisciplineLabel.textProperty().bind(resourceFactory.getStringBinding("NameDisciplineColumn"));
        lectureHoursDisciplineLabel.textProperty().bind(resourceFactory.getStringBinding("LectureHoursDisciplineColumn"));
        enterButton.textProperty().bind(resourceFactory.getStringBinding("EnterButton"));
    }
}

















