package common.collection;




import common.data.LabWork;
import common.exceptions.CannotAddException;
import common.exceptions.EmptyCollectionException;
import common.exceptions.NoSuchIdException;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;


/**
 * Operates collection.
 */
public abstract class LabWorkManagerImpl<T extends Collection<LabWork>> implements LabWorkManager {

    private final java.time.LocalDateTime initDate;

    /**
     * Constructor, set start values
     */
    public LabWorkManagerImpl() {
        initDate = java.time.LocalDateTime.now();
    }

    public int generateNextId() {
        if (getCollection().isEmpty())
            return 1;
        else {
            int id = 1;
            if (getUniqueIds().contains(id)) {
                while (getUniqueIds().contains(id)) id += 1;
            }
            return id;
        }
    }

    public void sort() {
        //collection = collection.stream().sorted(new Worker.SortingComparator()).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Return collection
     *
     * @return Collection
     */
    public abstract Collection<LabWork> getCollection();


    /**
     * Add element to collection
     *
     * @param labWork Element of collection
     */
    public void add(LabWork labWork) {
        int id = generateNextId();
        getUniqueIds().add(id);
        labWork.setId(id);
        getCollection().add(labWork);
    }

    public LabWork getByID(Integer id){
        assertNotEmpty();
        Optional<LabWork> labWork = getCollection().stream()
                .filter(w -> w.getId() == id)
                .findFirst();
        if(!labWork.isPresent()){
            throw new NoSuchIdException(id);
        }
        return labWork.get();
    }
    protected void addWithoutIdGeneration(LabWork worker){
        getUniqueIds().add(worker.getId());
        getCollection().add(worker);
    }

    protected void removeAll(Collection<Integer> ids){
        Iterator<Integer> iterator = ids.iterator();
        while (iterator.hasNext()){
            Integer id = iterator.next();
            getCollection().removeIf(labWork -> labWork.getId()==id);
            iterator.remove();
        }
    }

    /**
     * Get information about collection
     *
     * @return Information
     */
    public String getInfo() {
        return "[DatabaseInfo] Database of LabWork, size: [" + getCollection().size() + "] ; initialization date: [" + initDate.toString()+"]";
    }

    /**
     * Give info about is this ID used
     *
     * @param ID ID
     * @return is it used or not
     */
    public boolean checkID(Integer ID) {
        return getUniqueIds().contains(ID);
    }

    public void assertNotEmpty(){
        if(getCollection().isEmpty()) throw new EmptyCollectionException();
    }
    /**
     * Delete element by ID
     *
     * @param id ID
     */

    public void removeByID(Integer id) {
        assertNotEmpty();
        Optional<LabWork> worker = getCollection().stream()
                .filter(w -> w.getId() == id)
                .findFirst();
        if(!worker.isPresent()){
            throw new NoSuchIdException(id);
        }
        getCollection().remove(worker.get());
        getUniqueIds().remove(id);
    }

    /**
     * Delete element by ID
     *
     * @param id ID
     */
    public void updateByID(Integer id, LabWork newLabWork) {
        assertNotEmpty();
        Optional<LabWork> labWork = getCollection().stream()
                .filter(w -> w.getId() == id)
                .findFirst();
        if (!labWork.isPresent()) {
            throw new NoSuchIdException(id);
        }
        getCollection().remove(labWork.get());
        newLabWork.setId(id);
        getCollection().add(newLabWork);

    }

    /**
     * Get size of collection
     *
     * @return Size of collection
     */
    public int getSize() {
        return getCollection().size();
    }


    public void clear() {
        getCollection().clear();
        getUniqueIds().clear();
    }

    public void removeFirst() {
        assertNotEmpty();
        Iterator<LabWork> it =  getCollection().iterator();
        int id = it.next().getId();
        it.remove();
        getUniqueIds().remove(id);
    }

    /**
     * Add if ID of element bigger than max in collection
     *
     * @param labWork Element
     */
    public void addIfMax(LabWork labWork) {
        if (getCollection().stream()
                .max(LabWork::compareTo)
                .filter(w -> w.compareTo(labWork) == 1)
                .isPresent()) {
            throw new CannotAddException();
        }
        add(labWork);
    }

    /**
     * Add if ID of element smaller than min in collection
     *
     * @param labWork Element
     */
    public void addIfMin(LabWork labWork) {
        if (getCollection().stream()
                .min(LabWork::compareTo)
                .filter(w -> w.compareTo(labWork) < 0)
                .isPresent()) {
            throw new CannotAddException();
        }
        add(labWork);
    }

    public List<LabWork> filterStartsWithName(String start) {
        assertNotEmpty();
        return getCollection().stream()
                .filter(w -> w.getName().startsWith(start.trim()))
                .collect(Collectors.toList());
    }





    @Override
    public void deserializeCollection(String data) {

    }

    @Override
    public String serializeCollection() {
        return null;
    }
    abstract public Set<Integer> getUniqueIds();
}