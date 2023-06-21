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
        Collection<LabWork> changes = response.getCollection();

        if (op==CollectionOperation.CLEAR) {
            super.clear();
        }

        if(op==CollectionOperation.ADD){
            for(LabWork labWork: changes){
                super.addWithoutIdGeneration(labWork);
            }
        }
        if(op==CollectionOperation.REMOVE){
            for(LabWork labWork: changes){
                super.removeByID(labWork.getId());
            }
        }
        if(op==CollectionOperation.UPDATE){
            for(LabWork labWork: changes){
                super.updateByID(labWork.getId(),labWork);
            }
        }
        if(controller!=null && op!=CollectionOperation.NONE && changes!=null && !changes.isEmpty()){
            Platform.runLater(()->{

                controller.refreshCanvas();
                controller.refreshTable();
            });
        }

    }
    public ObservableList<LabWork> getCollection(){
        return collection;
    }
    @Override
    public void updateByID(Integer id, LabWork newWorker) {
        assertNotEmpty();
        Optional<LabWork> labWork = getCollection().stream()
                .filter(w -> w.getId() == id)
                .findFirst();
        if (!labWork.isPresent()) {
            throw new NoSuchIdException(id);
        }
        Collections.replaceAll(collection,labWork.get(),newWorker);
    }

    public void setController(MainWindowController c){
        controller = c;
    }
    public MainWindowController getController(){
        return controller;
    }

    public void messageInfo(String str) {
        Platform.runLater(() -> controller.messageInfo(str));
    }

    public void messageError(String str) {
        Platform.runLater(() -> controller.messageError(str));
    }
}
