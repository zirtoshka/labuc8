package server.collection;
import common.data.LabWork;

import java.util.Collection;
import java.util.List;
import java.util.Stack;

/**
 * interface for storing elements
 * @param <T> type of elements
 */
public interface CollectionManager<T> {
    /**
     * generates new unique id for collection
     * @return
     */
    public Integer generateNextId();

    public void sort();

    public Collection<T> getCollection();

    /**
     * adds new element
     * @param element
     */
    public void add(LabWork element);

    /**
     * get information about collection
     * @return info
     */
    public String getInfo();

    /**
     * checks if collection contains element with particular id
     * @param id
     * @return
     */
    public boolean checkId(Integer id);

    /**
     * removes element by id
     * @param id
     */
    public boolean removeById(Integer id);

    /**
     * updates element by id
     * @param id
     * @param newElement
     */
    public boolean updateById(Integer id, T newElement);

    /**
     * get collection size
     * @return
     */
    public int getSize();

    public void clear();

    public LabWork getById(Integer id);
    /**
     * adds element if it is greater than max
     * @param element
     */
    public boolean addIfMax(T element);

    /**
     * Remove elements in collection if id of elements smaller then that
     * @param newLabWork Element
     */
    public boolean removeLower(LabWork newLabWork);

    /**
     * Print if field personalQualitiesMinimum is min in collection
     *
     * @return
     */
    public String minByPersonalQualitiesMinimum();

    /**
     * Print if field discipline is max in collection
     *
     * @return
     */
    public String maxByDiscipline();

    /**
     * filter all elements which name starts with substring
     * @param start
     */
    public List<LabWork> filterStartsWithName(String start);

    /**
     * convert collection to json
     * @param json
     * @return true if success, false if not
     */
    public boolean deserializeCollection(String json);

    /**
     * parse collection from json
     * @return
     */
    public String serializeCollection();
}
