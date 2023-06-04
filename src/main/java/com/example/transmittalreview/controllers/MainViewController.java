package com.example.transmittalreview.controllers;

import com.example.transmittalreview.controllers.entities.TablePart;
import com.example.transmittalreview.model.dao.ApplicationSettings;
import com.example.transmittalreview.model.entities.Part;
import com.example.transmittalreview.model.service.BOMService;
import com.example.transmittalreview.model.service.ComparisonReport;
import com.example.transmittalreview.model.service.ApplicationSettingsService;
import com.example.transmittalreview.model.service.TransmittalService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.Clipboard;
import javafx.stage.FileChooser;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainViewController {
    public TableView<TablePart> leftTableView;
    public TableColumn<TablePart, String> leftPartNumberColumn;
    public TableColumn<TablePart, String> leftFlagColumn;
    public TableView<TablePart> rightTableView;
    public TableColumn<TablePart, String> rightPartNumberColumn;
    public TableColumn<TablePart, String> rightFlagColumn;
    public Button leftClipboard;
    public Button rightClipboard;
    public Button rightSelectFile;
    public Button leftSelectFile;
    
    private List<Part> leftBom = new ArrayList<>();
    private List<Part> rightBom = new ArrayList<>();
    private final ObservableList<TablePart> leftData = FXCollections.observableArrayList();
    private final ObservableList<TablePart> rightData = FXCollections.observableArrayList();
    private final Clipboard clipboard = Clipboard.getSystemClipboard();
    
    public void initialize() {
        leftPartNumberColumn.setCellValueFactory(new PropertyValueFactory<>("part"));
        leftTableView.setItems(leftData);
        rightPartNumberColumn.setCellValueFactory(new PropertyValueFactory<>("part"));
        rightTableView.setItems(rightData);
        
        generateDefaultIfMissing();
        
        setupRowColors();
    }
    
    public void leftClipboardClicked(ActionEvent actionEvent) {
        String clipboardData = clipboard.getString();
        leftBom = new BOMService(clipboardData).getParts();
        
        updateTableStatuses();
        
        rightTableView.refresh(); // Update Color changes
    }
    
    public void rightClipboardClicked(ActionEvent actionEvent) {
        String clipboardData = clipboard.getString();
        rightBom = new BOMService(clipboardData).getParts();
        
        updateTableStatuses();
        
        leftTableView.refresh();// Update Color changes
    }
    
    private void updateTableStatuses(){
        setLeftTableData();
        setRightTableData();
    }
    
    private void setLeftTableData(){
        List<TablePart> tableParts = new ArrayList<>();
        
        leftData.clear();
        
        ComparisonReport.generateReport(leftBom,rightBom)
                .forEach((part, status) -> tableParts.add(
                        TablePart.builder()
                                .part(part)
                                .status(status)
                                .build()
                ));
        
        leftData.addAll(tableParts);
    }
    
    private void setRightTableData(){
        List<TablePart> tableParts = new ArrayList<>();
        
        rightData.clear();
        
        ComparisonReport.generateReport(rightBom, leftBom)
                .forEach(((part, status) -> tableParts.add(
                        TablePart.builder()
                                .part(part)
                                .status(status)
                                .build()
                )));
        rightData.addAll(tableParts);
    }
    
    private void setupRowColors(){
        leftTableView.setRowFactory(tv -> new TableRow<>() {
            public void updateItem(TablePart tablePart, boolean empty) {
                super.updateItem(tablePart, empty);
                if (tablePart == null){
                    setStyle("-fx-background-color: #303342");
                    return;
                }
                switch (tablePart.getStatus()){
                    case CORRECT -> setStyle("-fx-background-color: green");
                    case MISMATCH -> setStyle("-fx-background-color: yellow");
                    case MISSING -> setStyle("-fx-background-color: red");
                    case DEFAULT -> setStyle("-fx-background-color: gray");
                }
            }
        });
    
        rightTableView.setRowFactory(tv -> new TableRow<>() {
            public void updateItem(TablePart tablePart, boolean empty) {
                super.updateItem(tablePart, empty);
                if (tablePart == null){
                    setStyle("-fx-background-color: #303342");
                    return;
                }
                switch (tablePart.getStatus()){
                    case CORRECT -> setStyle("-fx-background-color: green");
                    case MISMATCH -> setStyle("-fx-background-color: yellow");
                    case MISSING -> setStyle("-fx-background-color: red");
                    case DEFAULT -> setStyle("-fx-background-color: gray");
                }
            }
        });
    }
    
    public void leftSelectFileClicked(ActionEvent actionEvent) {
//        ApplicationSettings applicationSettings = ApplicationSettingsService.loadSettings(new File("/home/kal/IdeaProjects/GitProjects/TransmittalReview/src/test/resources/transmittalReview/Transmittal/Wheeled_Coach.json"));
//        leftBom =   TransmittalService.builder()
//                .settings(applicationSettings)
//                .file(fileFromChooser())
//                .build()
//                .getParts();
//
//        System.out.println(leftBom);
//
//        updateTableStatuses();
        
        
    }
    
    public void rightSelectFileClicked(ActionEvent actionEvent) {
    }
    
    private void generateDefaultIfMissing(){
    
    }
    
    private File fileFromChooser(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Transmittal");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Excel Files", "*.xlsm"),
                new FileChooser.ExtensionFilter("All files", "*.*")
        );
        
        return fileChooser.showOpenDialog(null);
    }
 
}