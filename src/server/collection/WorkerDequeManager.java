package server.collection;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import common.collection.WorkerManager;
import common.collection.WorkerManagerImpl;
import common.data.Worker;
import common.exceptions.CannotAddException;
import common.exceptions.CollectionException;
import common.exceptions.EmptyCollectionException;
import common.exceptions.NoSuchIdException;
import server.json.*;


import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.stream.Collectors;


/**
 * Operates collection.
 */
public class WorkerDequeManager extends WorkerManagerImpl<ConcurrentLinkedDeque<Worker>>{
    private Deque<Worker> collection;
    private final java.time.LocalDateTime initDate;
    private final Set<Integer> uniqueIds;

    /**
     * Constructor, set start values
     */
    public WorkerDequeManager() {
        uniqueIds = new ConcurrentSkipListSet<>();
        collection = new ConcurrentLinkedDeque<>();
        initDate = java.time.LocalDateTime.now();
    }
    public Set<Integer> getUniqueIds(){
        return uniqueIds;
    }

    public int generateNextId() {
        if (collection.isEmpty())
            return 1;
        else {
            int id = collection.element().getId() + 1;
            if (uniqueIds.contains(id)) {
                while (uniqueIds.contains(id)) id += 1;
            }
            return id;
        }
    }
    @Override
    public Deque<Worker> getCollection(){
        return collection;
    }

    public void sort() {
        collection = collection.stream().sorted(new Worker.SortingComparator()).collect(Collectors.toCollection(ConcurrentLinkedDeque::new));
    }



    /**
     * Add element to collection
     *
     * @param worker Element of collection
     */
    public void add(Worker worker) {
        int id = generateNextId();
        uniqueIds.add(id);
        worker.setId(id);
        collection.add(worker);
    }

    public Worker getByID(Integer id){
        assertNotEmpty();
        Optional<Worker> worker = collection.stream()
                .filter(w -> w.getId() == id)
                .findFirst();
        if(!worker.isPresent()){
            throw new NoSuchIdException(id);
        }
        return worker.get();
    }
    protected void addWithoutIdGeneration(Worker worker){
        uniqueIds.add(worker.getId());
        collection.add(worker);
    }

    protected Collection<Worker> getAll(Collection<Integer> ids){
        Iterator<Integer> iterator = ids.iterator();
        Collection<Worker> selected = new HashSet<>();
        while (iterator.hasNext()){
            Integer id = iterator.next();
            selected.addAll(collection.stream().filter(w->w.getId()==id).collect(Collectors.toCollection(HashSet::new)));
            iterator.remove();
        }
        return selected;
    }
    protected void removeAll(Collection<Integer> ids){
        Iterator<Integer> iterator = ids.iterator();
        while (iterator.hasNext()){
            Integer id = iterator.next();
            collection.removeIf(worker -> worker.getId()==id);
            iterator.remove();
        }
    }


    /**
     * Give info about is this ID used
     *
     * @param ID ID
     * @return is it used or not
     */
    public boolean checkID(Integer ID) {
        return uniqueIds.contains(ID);
    }

    public void assertNotEmpty(){
        if(collection.isEmpty()) throw new EmptyCollectionException();
    }
    /**
     * Delete element by ID
     *
     * @param id ID
     */

    public void removeByID(Integer id) {
        assertNotEmpty();
        Optional<Worker> worker = collection.stream()
                .filter(w -> w.getId() == id)
                .findFirst();
        if(!worker.isPresent()){
            throw new NoSuchIdException(id);
        }
        collection.remove(worker.get());
        uniqueIds.remove(id);
    }

    /**
     * Delete element by ID
     *
     * @param id ID
     */
    public void updateByID(Integer id, Worker newWorker) {
        assertNotEmpty();
        Optional<Worker> worker = collection.stream()
                .filter(w -> w.getId() == id)
                .findFirst();
        if (!worker.isPresent()) {
            throw new NoSuchIdException(id);
        }
        collection.remove(worker.get());
        newWorker.setId(id);
        collection.add(newWorker);
    }

    /**
     * Get size of collection
     *
     * @return Size of collection
     */
    public int getSize() {
        return collection.size();
    }


    public void clear() {
        collection.clear();
        uniqueIds.clear();
    }

    public void removeFirst() {
        assertNotEmpty();
        int id = collection.getFirst().getId();
        collection.removeFirst();
        uniqueIds.remove(id);
    }


    public void deserializeCollection(String json) {
        try {
            if (json == null || json.equals("")) {
                collection = new ConcurrentLinkedDeque<>();
            } else {
                Type collectionType = new TypeToken<Queue<Worker>>() {
                }.getType();
                Gson gson = new GsonBuilder()
                        .registerTypeAdapter(LocalDate.class, new LocalDateDeserializer())
                        .registerTypeAdapter(Date.class, new DateDeserializer())
                        .registerTypeAdapter(collectionType, new CollectionDeserializer(uniqueIds))
                        .create();
                collection = gson.fromJson(json.trim(), collectionType);
            }
        } catch (JsonParseException e) {
            throw new CollectionException("cannot load");
        }
    }

    public String serializeCollection() {
        if (collection == null || collection.isEmpty()) return "";
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateSerializer())
                .registerTypeAdapter(Date.class, new DateSerializer())
                .setPrettyPrinting().create();
        return gson.toJson(collection);
    }
}