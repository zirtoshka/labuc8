
package common.collection;

import common.data.LabWork;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * interface for storing elements
 *
 */
public interface LabWorkManager {

    /**
     * sorts collection
     */
    void sort();

    Collection<LabWork> getCollection();

    /**
     * adds new element
     *
     * @param element
     */
    void add(LabWork element);

    /**
     * get information about collection
     *
     * @return info
     */
    String getInfo();

    /**
     * checks if collection contains element with particular id
     *
     * @param ID
     * @return flag
     */
    boolean checkID(Integer ID);

    /**
     * removes element by id
     *
     * @param id
     */
    void removeByID(Integer id);

    /**
     * updates element by id
     *
     * @param id
     * @param newElement
     */
    void updateByID(Integer id, LabWork newElement);


    void clear();

    public LabWork getByID(Integer id);

    void removeFirst();

    /**
     * adds element if it is greater than max
     *
     * @param element
     */
    void addIfMax(LabWork element);

    /**
     * adds element if it is smaller than min
     *
     * @param element
     */
    void addIfMin(LabWork element);

    /**
     * print all elements which name starts with substring
     *
     * @param start
     */
    List<LabWork> filterStartsWithName(String start);

    /**
     * @return map of dates with quantity
     */
    Map<LocalDate, Integer> groupByEndDate();

    /**
     * print all unique values of salary field
     */
    List<Long> getUniqueSalaries();

    /**
     * convert collection to json
     *
     * @param data
     */
    void deserializeCollection(String data);

    /**
     * parse collection from json
     *
     * @return serialized collection
     */
    String serializeCollection();

}
