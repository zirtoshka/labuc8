package client.collection;

import client.controllers.MainWindowController;
import common.collection.LabWorkManagerImpl;
import common.connection.CollectionOperation;
import common.connection.Response;

import common.data.LabWork;
import common.exceptions.NoSuchIdException;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class LabWorkObservableManager extends LabWorkManagerImpl<ObservableList<LabWork>> {
    private ObservableList<LabWork> collection;
    private Set<Integer> uniqueIds;
    private MainWindowController controller;
    public LabWorkObservableManager(){
        collection = FXCollections.observableArrayList();
        uniqueIds = ConcurrentHashMap.newKeySet();
    }

    public Set<Integer> getUniqueIds(){
        return uniqueIds;
    }
    public void applyChanges(Response response){
        CollectionOperation op = response.getCollectionOperation();
        Collection<Worker> changes = response.getCollection();

        if(op==CollectionOperation.ADD){
            for(Worker worker: changes){
                super.addWithoutIdGeneration(worker);
            }
        }
        if(op==CollectionOperation.REMOVE){
            for(Worker worker: changes){
                super.removeByID(worker.getId());
            }
        }
        if(op==CollectionOperation.UPDATE){
            for(Worker worker: changes){
                super.updateByID(worker.getId(),worker);
            }
        }
        if(controller!=null && op!=CollectionOperation.NONE && changes!=null && !changes.isEmpty()){
            Platform.runLater(()->{

                controller.refreshCanvas();
                controller.refreshTable();
            });
        }

    }
    public ObservableList<Worker> getCollection(){
        return collection;
    }
    @Override
    public void updateByID(Integer id, Worker newWorker) {
        assertNotEmpty();
        Optional<Worker> worker = getCollection().stream()
                .filter(w -> w.getId() == id)
                .findFirst();
        if (!worker.isPresent()) {
            throw new NoSuchIdException(id);
        }
        Collections.replaceAll(collection,worker.get(),newWorker);
    }

    public void setController(MainWindowController c){
        controller = c;
    }
    public MainWindowController getController(){
        return controller;
    }
}
