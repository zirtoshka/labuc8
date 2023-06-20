package server.collection;

import java.util.*;
import java.lang.reflect.Type;
import java.util.stream.Collectors;

import common.data.LabWork;
import common.exceptions.EmptyCollectionException;
import common.exceptions.NoSuchIdException;
import server.json.*;

import static common.io.OutputManager.*;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;


/**
 * Operates collection.
 */
public class LabWorkCollectionManager implements CollectionManager<LabWork>{
    private Stack<LabWork> collection;
    private final java.time.LocalDateTime initDate;
    private final HashSet<Integer> uniqueIds;
    private static final Integer THE_FIRST_ID = 1;

    /**
     * Constructor, set start values
     */
    public LabWorkCollectionManager()
    {
        uniqueIds = new HashSet<>();
        collection = new Stack<>();
        initDate = java.time.LocalDateTime.now();
    }

    public Integer generateNextId(){

        if (collection.isEmpty())
            return THE_FIRST_ID;
        else {
            Integer id = collection.peek().getId() == Integer.MAX_VALUE ?
                    THE_FIRST_ID :
                    collection.peek().getId() + 1;
            if (uniqueIds.contains(id)){
                while (uniqueIds.contains(id)) id+=1;
            }
            uniqueIds.add(id);
            return id;
        }
    }

    public void sort(){
        Collections.sort(collection, new LabWork.SortingComparator());
    }

    /**
     * Return collection
     * @return Collection
     */
    public Stack<LabWork> getCollection()
    {
        return collection;
    }

    /**
     * Add element to collection
     * @param labWork Element of collection
     */
    public void add(LabWork labWork){
        labWork.setId(generateNextId());
        collection.push(labWork);
        print("Added element:");
        print(labWork.toString());
    }

    @Override
    public LabWork getById(Integer id) {
        if(collection.isEmpty()) throw new EmptyCollectionException();
        Optional<LabWork> labWork = collection.stream()
                .filter(w -> w.getId() == id)
                .findFirst();
        if(!labWork.isPresent()){
            throw new NoSuchIdException(id);
        }
        return labWork.get();
    }

    protected void addWithoutIdGeneration(LabWork labWork){
        uniqueIds.add(labWork.getId());
        collection.add(labWork);
    }

    /**
     * Get information about collection
     * @return Information
     */
    public String getInfo(){
        return "Stack of LabWork, size: " + Integer.toString(collection.size()) + ", initialization date: " + initDate.toString();
    }

    /**
     * Give info about is this id used
     * @param id
     * @return is it used or not
     */
    public boolean checkId(Integer id){
        if (uniqueIds.contains(id)) return true;
        return false;
    }

    /**
     * Delete element by id
     * @param id
     */
    public boolean removeById(Integer id){
        if (checkId(id))
            for (LabWork labWork : collection){
                if (labWork.getId() == id){
                    collection.remove(labWork);
                    uniqueIds.remove(id);
                    //print("element #"+Integer.toString(id)+" successfully deleted");
                    return true;
                }
            }
        return false;
    }

    /**
     * Delete element by id
     * @param id
     */
    public boolean updateById(Integer id, LabWork newLabWork){
        int currentId = THE_FIRST_ID;
        for (LabWork labWork : collection){
            if (labWork.getId() == id){
                newLabWork.setId(id);
                collection.set(currentId - 1, newLabWork);
                //print("element #"+Integer.toString(id)+" successfully updated");
                return true;
            }
            currentId += 1;
        }
        return false;
    }

    /**
     * Get size of collection
     * @return Size of collection
     */
    public int getSize(){
        return collection.size();
    }


    public void clear(){
        collection.clear();
        uniqueIds.clear();
    }

    /**
     * Add if id of element bigger than max in collection
     * @param newLabWork Element
     */
    public boolean addIfMax(LabWork newLabWork){
        for (LabWork labWork : collection){
            if (newLabWork.compareTo(labWork) < 0){
                printErr("unable to add");
                return false;
            }
        }
        add(newLabWork);
        return true;
    }

    /**
     * Remove elements in collection if id of elements smaller then that
     * @param newLabWork Element
     */
    public boolean removeLower(LabWork newLabWork){
        boolean isHappened = false;
        for (LabWork labWork : collection){
            if (newLabWork.compareTo(labWork) > 0){
                collection.remove(labWork);
                isHappened = true;
            }
        }
        return isHappened;
    }

    /**
     * Print if field personalQualitiesMinimum is min in collection
     *
     * @return
     */
    public String minByPersonalQualitiesMinimum(){
        return collection.stream()
                .min(Comparator.comparing(LabWork::getPersonalQualitiesMinimum))
                .get().toString();
    }

    /**
     * Print if field discipline is max in collection
     *
     * @return
     */
    public String maxByDiscipline(){
        return collection.stream()
                .max(Comparator.comparingInt(value -> value.getDiscipline().getLectureHours()))
                .get().toString();
    }

    public List<LabWork> filterStartsWithName(String start){

        List<LabWork> list = collection.stream()
                .filter(labWork-> labWork.getName().startsWith(start.trim()))
                .collect(Collectors.toList());
        return list;
    }

    protected void removeAll(Collection<Integer> ids){
        Iterator<Integer> iterator = ids.iterator();
        while (iterator.hasNext()){
            Integer id = iterator.next();
            collection.removeIf(labWork -> labWork.getId()==id);
            iterator.remove();
        }
    }

    public boolean deserializeCollection(String jsonData){
        boolean success = true;
        try {
            if (jsonData == null || jsonData.equals("")){
                collection =  new Stack<LabWork>();
            } else {
                Type collectionType = new TypeToken<Stack<LabWork>>(){}.getType();
                Gson collectionData = new GsonBuilder()
                        .registerTypeAdapter(Date.class, new DateDeserializer())
                        .registerTypeAdapter(collectionType, new CollectionDeserializer(uniqueIds))
                        .create();
                collection = collectionData.fromJson(jsonData.trim(), collectionType);
                if (collection == null)
                    collection = new Stack<>();
            }
        } catch (JsonParseException e){
            success = false;
            printErr("wrong json data");
        }
        return success;
    }

    public String serializeCollection(){
        if (collection == null || collection.isEmpty()) return "";
        Gson collectionData = new GsonBuilder()
                .registerTypeAdapter(Date.class, new DateSerializer())
                .setPrettyPrinting().create();
        String jsonData = collectionData.toJson(collection);
        return jsonData;
    }
}
