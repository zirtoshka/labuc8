package common.io;

import common.auth.User;
import common.data.Coordinates;
import common.data.Difficulty;
import common.data.Discipline;
import common.data.LabWork;
import common.exceptions.InvalidFieldException;
import common.exceptions.InvalidInputCharacterException;
import common.exceptions.InvalidNumberException;

import java.util.Scanner;
public class ConsoleInputManager extends InputManagerImpl{

    public ConsoleInputManager(){
        super(new Scanner(System.in));
    }

    @Override
    public String readName() throws InvalidInputCharacterException {
        return new Question<String>("enter name (value must be not empty):", super::readName).getAnswer();
    }


    @Override
    public double readXCoord() throws InvalidInputCharacterException {
        return new Question<Double>("enter x:", super::readXCoord).getAnswer();
    }

    @Override
    public Integer readYCoord() throws InvalidInputCharacterException {
        return new Question<Integer>("enter y (value must be bigger than -545):", super::readYCoord).getAnswer();
    }

    @Override
    public Coordinates readCoords() throws InvalidNumberException, InvalidInputCharacterException {
        OutputManager.print("enter coordinates");
        double x = readXCoord();
        Integer y = readYCoord();
        Coordinates coord = new Coordinates(x,y);
        return coord;
    }

    @Override
    public Integer readMinimalPoint() throws InvalidInputCharacterException {
        return new Question<Integer>("enter minimal point (value must be bigger than 0):",super::readMinimalPoint).getAnswer();
    }

    @Override
    public int readPersonalQualitiesMinimum() throws InvalidInputCharacterException {
        return new Question<Integer>("enter personal qualities minimum (value must be bigger than 0):",super::readPersonalQualitiesMinimum).getAnswer();
    }

    @Override
    public Double readAveragePoint() throws InvalidInputCharacterException {
        return new Question<Double>("enter average point (value must be bigger than 0):",super::readAveragePoint).getAnswer();
    }

    @Override
    public Difficulty readDifficulty() throws InvalidInputCharacterException {
        return new Question<Difficulty>("enter difficulty(VERY_HARD, IMPOSSIBLE, HOPELESS, TERRIBLE):", super::readDifficulty).getAnswer();
    }

    @Override
    public Integer readLectureHours() throws InvalidInputCharacterException {
        return new Question<Integer>("enter lecture hours:",super::readLectureHours).getAnswer();
    }

    @Override
    public Discipline readDiscipline() throws InvalidFieldException, InvalidInputCharacterException {
        OutputManager.print("enter discipline");
        String name = readName();
        Integer lectureHours = readLectureHours();
        return new Discipline(name, lectureHours);
    }

    @Override
    public LabWork readLabWork() throws InvalidNumberException, InvalidFieldException, InvalidInputCharacterException {
        String name = readName();
        Coordinates coords = readCoords();
        Integer minimalPoint = readMinimalPoint();
        int personalQualitiesMinimum = readPersonalQualitiesMinimum();
        Double averagePoint = readAveragePoint();
        Difficulty difficulty = readDifficulty();
        Discipline discipline = readDiscipline();
        return new LabWork(name, coords, minimalPoint, personalQualitiesMinimum, averagePoint, difficulty, discipline);
    }

    @Override
    public String readLogin() {
        return new Question<String>("enter login:", super::readLogin).getAnswer();
    }

    @Override
    public String readPassword() {
        return new Question<String>("enter password:", super::readPassword).getAnswer();
    }

    @Override
    public User readUser() {
        String login = readLogin();
        String password = readPassword();
        return new User(login, password);
    }
}
