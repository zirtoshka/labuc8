package common.data;

import common.exceptions.InvalidNumberException;

import java.io.Serializable;

public class Coordinates implements Validateable, Serializable {
    private double x;
    private Integer y; //Значение поля должно быть больше -545, Поле не может быть null
    public Coordinates(double x, Integer y) throws InvalidNumberException {
        if (y == null || y <= -545 || Double.isInfinite(x) || Double.isNaN(x)) {
            throw new InvalidNumberException();
        }
        this.x = x;
        this.y = y;
    }

    /**
     * @return x coord
     */
    public double getX() {
        return x;
    }

    /**
     * @return y coord
     */
    public Integer getY() {
        return y;
    }

    @Override
    public String toString(){
        String s = "";
        s += "{\"x\" : " + Double.toString(x) + ", ";
        s += "\"y\" : " + Integer.toString(y) + "}";
        return s;
    }

    public boolean validate(){
        return (y != null && y > -545 && !Double.isInfinite(x) && !Double.isNaN(x));
    }
}
