package com.example.transmittalreview.controllers;

import com.example.transmittalreview.model.entities.Part;
import com.example.transmittalreview.model.entities.Status;
import com.example.transmittalreview.model.service.BOMService;
import com.example.transmittalreview.model.service.ComparisonReport;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.Clipboard;
import javafx.stage.FileChooser;
import org.apache.poi.ss.formula.functions.T;
import org.controlsfx.control.spreadsheet.ClipboardCell;

import java.io.File;

public class MainViewController {
    public TableView<Part> firstTableView;
    public TableColumn<Part, String> firstPartNumberColumn;
    public TableColumn<Part, String> firstFlagColumn;
    public TableView<Part> secondTableView;
    public TableColumn<Part, String> secondPartNumberColumn;
    public TableColumn<Part, String> secondFlagColumn;
    public Button firstClipboard;
    public Button secondClipboard;
    public Button secondSelectFile;
    public Button firstSelectFile;
    @FXML
    private Label welcomeText;
    private final ObservableList<Part> firstData = FXCollections.observableArrayList();
    private final ObservableList<Part> secondData = FXCollections.observableArrayList();
    private BOMService firstBom;
    private BOMService secondBom;
    private Clipboard clipboard = Clipboard.getSystemClipboard();
    
    public void initialize() {
        firstPartNumberColumn.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        firstTableView.setItems(firstData);
        secondPartNumberColumn.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        secondTableView.setItems(secondData);
        setupRowColors();
        
    }
    
    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
    
    
    public void firstClipboardClicked(ActionEvent actionEvent) {
        String clipboardData = clipboard.getString();
        firstBom = new BOMService(clipboardData);
        firstData.clear();
        firstData.addAll(firstBom.getBom().getDrawings());
        runComparison();
        secondTableView.refresh(); // Update Color changes
    }
    
    public void secondClipboardClicked(ActionEvent actionEvent) {
        String clipboardData = clipboard.getString();
        secondBom = new BOMService(clipboardData);
        secondData.clear();
        secondData.addAll(secondBom.getBom().getDrawings());
        runComparison();
        firstTableView.refresh(); // Update Color changes
    }
    
    private void runComparison(){
        if (!(firstData.isEmpty() || secondData.isEmpty())){
            //TODO: Definitely rewrite as a static class that runs once and does this method backwards and forwards
            ComparisonReport comparisonReport = new ComparisonReport(firstBom.getBom(), secondBom.getBom());
            ComparisonReport comparisonReport1 = new ComparisonReport(secondBom.getBom(), firstBom.getBom());
            comparisonReport.report();
            comparisonReport1.report();
            firstData.clear();
            firstData.addAll(firstBom.getBom().getDrawings());
            secondData.clear();
            secondData.addAll(secondBom.getBom().getDrawings());
            System.out.println("ran");
        }
    }
    
    private void setupRowColors(){
        firstTableView.setRowFactory(tv -> new TableRow<>() {
            public void updateItem(Part part, boolean empty) {
                super.updateItem(part, empty);
                if (part == null){
                    setStyle("-fx-background-color: #303342");
                    return;
                }
                switch (part.getStatus()){
                    case CORRECT -> setStyle("-fx-background-color: green");
                    case MISMATCH -> setStyle("-fx-background-color: yellow");
                    case MISSING -> setStyle("-fx-background-color: red");
                    case DEFAULT -> setStyle("-fx-background-color: gray");
                }
            }
        });
    
        secondTableView.setRowFactory(tv -> new TableRow<>() {
            public void updateItem(Part part, boolean empty) {
                super.updateItem(part, empty);
                if (part == null){
                    setStyle("-fx-background-color: #303342");
                    return;
                }
                switch (part.getStatus()){
                    case CORRECT -> setStyle("-fx-background-color: green");
                    case MISMATCH -> setStyle("-fx-background-color: yellow");
                    case MISSING -> setStyle("-fx-background-color: red");
                    case DEFAULT -> setStyle("-fx-background-color: gray");
                }
            }
        });
    }
    
    public void firstSelectFileClicked(ActionEvent actionEvent) {
//        FileChooser fileChooser = new FileChooser();
//        File chosen = new File(fileChooser.getInitialFileName());
//        firstBom = new BOMService(chosen);
//        firstData.clear();
//        firstData.addAll(firstBom.getBom().getParts());
//        runComparison();
//        firstTableView.refresh();
    }
    
    public void secondSelectFileClicked(ActionEvent actionEvent) {
    }
    
 
}