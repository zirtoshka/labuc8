package common.data;

import common.auth.User;
import common.exceptions.InvalidFieldException;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;

/**
 * LabWork class
 */
public class LabWork implements Collectionable, Serializable {
    private Integer id; //Поле не может быть null, Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private String name; //Поле не может быть null, Строка не может быть пустой
    private Coordinates coordinates; //Поле не может быть null
    private java.util.Date creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    private Integer minimalPoint; //Поле не может быть null, Значение поля должно быть больше 0
    private int personalQualitiesMinimum; //Значение поля должно быть больше 0
    private Double averagePoint; //Поле может быть null, Значение поля должно быть больше 0
    private Difficulty difficulty; //Поле не может быть null
    private Discipline discipline; //Поле не может быть null
    private String userLogin;

    /**
     * constructor, just set fields
     *
     * @param name
     * @param coordinates
     * @param minimalPoint
     * @param personalQualitiesMinimum
     * @param averagePoint
     * @param difficulty
     * @param discipline
     */
    public LabWork(String name, Coordinates coordinates, Integer minimalPoint, int personalQualitiesMinimum, Double averagePoint, Difficulty difficulty, Discipline discipline) throws InvalidFieldException {
        if (
                coordinates == null || !coordinates.validate() ||
                        discipline == null || !discipline.validate() ||
                        (personalQualitiesMinimum <= 0) ||
                        (averagePoint != null && (averagePoint <= 0)) ||
                        minimalPoint == null || (minimalPoint <= 0) ||
                        name == null || name.equals("") ||
                        difficulty == null
        ) {
            throw new InvalidFieldException();
        }
        creationDate = new java.util.Date();
        this.name = name;
        this.coordinates = coordinates;
        this.minimalPoint = minimalPoint;
        this.personalQualitiesMinimum = personalQualitiesMinimum;
        this.averagePoint = averagePoint;
        this.difficulty = difficulty;
        this.discipline = discipline;
    }

    public static class SortingComparator implements Comparator<LabWork> {
        public int compare(LabWork first, LabWork second) {
            return first.getName().compareTo(second.getName());
        }
    }

    /**
     * @return int
     */
    public int getId() {
        return id;
    }


    /**
     * sets id, usefull for replacing elements in collection
     *
     * @param id
     */
    public void setId(int id) {
        if (id > 0) {
            this.id = id;
        }
    }


    public void setCreationDate(Date date) {
        creationDate = date;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String login) {
        userLogin = login;
    }

    public void setUser(User usr) {
        userLogin = usr.getLogin();
    }

    /**
     * @return String
     */
    public String getName() {
        return name;
    }


    /**
     * @return Integer
     */
    public Integer getMinimalPoint() {
        return minimalPoint;
    }

    /**
     * @return int
     */
    public int getPersonalQualitiesMinimum() {
        return personalQualitiesMinimum;
    }


    /**
     * @return Double
     */
    public Double getAveragePoint() {
        return averagePoint;
    }

    /**
     * @return Coordinates
     */
    public Coordinates getCoordinates() {
        return coordinates;
    }

    /**
     * @return Difficulty
     */
    public Difficulty getDifficulty() {
        return difficulty;
    }

    /**
     * @return Discipline
     */
    public Discipline getDiscipline() {
        return discipline;
    }

    /**
     * @return String
     */
    @Override
    public String toString() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String strCreationDate = dateFormat.format(creationDate);
        String s = "";
        s += "{\n";
        s += String.format("  \"id\" : %s,\n", Integer.toString(id));
        s += String.format("  \"name\" : %s,\n", name);
        s += String.format("  \"coordinates\" : %s,\n", coordinates.toString());
        s += String.format("  \"creationDate\" : %s,\n", strCreationDate);
        s += String.format("  \"minimalPoint\" : %s,\n", Integer.toString(minimalPoint));
        s += String.format("  \"personalQualitiesMinimum\" : %s,\n", Integer.toString(personalQualitiesMinimum));
        s += String.format("  \"averagePoint\" : %s,\n", Double.toString(averagePoint));
        s += String.format("  \"difficulty\" : %s,\n", difficulty.toString());
        s += String.format("  \"discipline\" : %s\n", discipline.toString());
        if (userLogin != null) s += "  \"userLogin\" : " + userLogin + "\n";
        s += "}";
        return s;
    }

    /**
     * @param obj
     * @return boolean
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null || this.getClass() != obj.getClass()) return false;
        LabWork another = (LabWork) obj;
        return this.getId() == another.getId();
    }


    /**
     * @param labWork
     * @return int
     */
    public int compareTo(Collectionable labWork) {
        int res = Long.compare(this.id, labWork.getId());
        return res;
    }

    /**
     * @return boolean
     */
    @Override
    public boolean validate() {
        return (
                coordinates != null && coordinates.validate() &&
                        discipline != null && discipline.validate() &&
                        (personalQualitiesMinimum > 0) &&
                        (averagePoint == null || (averagePoint > 0)) &&
                        minimalPoint != null && (minimalPoint > 0) &&
                        id != null && (id > 0) &&
                        name != null && !name.equals("") &&
                        difficulty != null &&
                        creationDate != null
        );
    }
}