package common.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import common.exceptions.InvalidDateFormatException;
/**
 * Provides methods to convenient conversion between String and Date
 */
public class DateConverter {
    private static String pattern = "yyyy-MM-dd";
    private static DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
    private static DateTimeFormatter localDateFormatter = DateTimeFormatter.ofPattern(pattern);

    /**
     * convert Date to String
     * @param date
     * @return
     */
    public static String dateToString(Date date){
        return dateFormatter.format(date);
    }

    /**
     * convert Date to String
     * @param s
     * @return String
     * @throws InvalidDateFormatException
     */
    public static Date parseDate(String s) throws InvalidDateFormatException{
        try{
            return dateFormatter.parse(s);
        }
        catch (ParseException e){
            throw new InvalidDateFormatException();
        }
    }
    public static void setPattern(String p){
        pattern = p;
        dateFormatter = new SimpleDateFormat(pattern);
        localDateFormatter = DateTimeFormatter.ofPattern(pattern);
    }
}
