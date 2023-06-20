package common.io;

import java.util.Scanner;

import common.auth.User;
import common.connection.CommandMsg;
import common.data.*;
import common.exceptions.*;

public interface InputManager {
    /**
     * reads name from input
     * @return
     * @throws EmptyStringException
     */
    public String readName() throws EmptyStringException, InvalidInputCharacterException;

    /**
     * reads x from input
     * @return
     * @throws InvalidNumberException
     */
    public double readXCoord() throws InvalidNumberException, InvalidInputCharacterException;

    /**
     * reads y from input
     * @return
     * @throws InvalidNumberException
     */
    public Integer readYCoord() throws InvalidNumberException, InvalidInputCharacterException;

    /**
     * reads coordinates from input
     * @return
     * @throws InvalidNumberException
     */
    public Coordinates readCoords() throws InvalidNumberException, InvalidInputCharacterException;

    /**
     * reads minimal point from input
     * @return
     * @throws InvalidNumberException
     */
    public Integer readMinimalPoint() throws InvalidNumberException, InvalidInputCharacterException;

    /**
     * reads personal qualities minimum from input
     * @return
     * @throws InvalidNumberException
     */
    public int readPersonalQualitiesMinimum() throws InvalidNumberException, InvalidInputCharacterException;

    /**
     * reads average point from input
     * @return
     * @throws InvalidNumberException
     */
    public Double readAveragePoint() throws InvalidNumberException, InvalidInputCharacterException;

    /**
     * reads position from input
     * @return
     * @throws InvalidEnumException
     */
    public Difficulty readDifficulty() throws InvalidEnumException, InvalidInputCharacterException;

    /**
     * reads lecture hours from input
     * @return
     * @throws InvalidNumberException
     */
    public Integer readLectureHours() throws InvalidNumberException, InvalidInputCharacterException;

    /**
     * reads organization from input
     * @return
     * @throws InvalidDataException
     */
    public Discipline readDiscipline() throws InvalidDataException;

    /**
     * reads LabWork from input
     * @return
     * @throws InvalidDataException
     */
    public LabWork readLabWork() throws InvalidDataException;

    /**
     * reads command-argument pair from input
     *
     * @return
     */
    public CommandMsg readCommand() throws InvalidInputCharacterException;

    /**
     * gets input scanner
     * @return
     */
    boolean hasNextLine();


    String readPassword() throws InvalidDataException;

    String readLogin() throws InvalidDataException;

    User readUser() throws InvalidDataException;

    Scanner getScanner();
}
