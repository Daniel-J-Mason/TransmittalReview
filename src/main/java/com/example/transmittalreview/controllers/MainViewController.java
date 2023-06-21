package com.example.transmittalreview.controllers;

import com.example.transmittalreview.controllers.components.TablePart;
import com.example.transmittalreview.model.dao.ApplicationSettings;
import com.example.transmittalreview.model.dao.TransmittalSettings;
import com.example.transmittalreview.model.entities.Part;
import com.example.transmittalreview.model.service.*;
import javafx.application.HostServices;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.Clipboard;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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
    
    private ApplicationSettings applicationSettings;
    private TransmittalSettings leftTransmittalSettings;
    private TransmittalSettings rightTransmittalSettings;
    private List<Part> leftBom = new ArrayList<>();
    private List<Part> rightBom = new ArrayList<>();
    private final ObservableList<TablePart> leftData = FXCollections.observableArrayList();
    private final ObservableList<TablePart> rightData = FXCollections.observableArrayList();
    private final Clipboard clipboard = Clipboard.getSystemClipboard();
    private HostServices hostServices;
    
    public void initialize() {
        initializeTables();
        generateDefaultsIfMissing();
        loadDefaultSettings();
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
    
    //TODO: .bom -> .xlsm context
    public void leftSelectFileClicked(ActionEvent actionEvent) {
        File transmittal = fileFromChooser();
        leftBom = TransmittalService.builder()
                .transmittalSettings(leftTransmittalSettings)
                .build()
                .getParts(transmittal);
        
        updateTableStatuses();
    }
    
    
    public void rightSelectFileClicked(ActionEvent actionEvent) {
        File transmittal = fileFromChooser();
        rightBom = TransmittalService.builder()
                .transmittalSettings(rightTransmittalSettings)
                .build()
                .getParts(transmittal);
        
        updateTableStatuses();
    }

    private void updateTableStatuses() {
        setLeftTableData();
        setRightTableData();
    }

    //TODO: Further split method to setStatuses() and addHyperlinks(), update tablePart for Hlink, Left-click file, right-click parent
    private void setLeftTableData() {
        List<TablePart> tableParts = new ArrayList<>();

        leftData.clear();

        ComparisonReport.generateReport(leftBom, rightBom)
                .forEach((part, status) -> tableParts.add(
                        TablePart.builder()
                                .part(part)
                                .status(status)
                                .build()
                ));

        leftData.addAll(tableParts);
    }

    private void setRightTableData() {
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
    
    private void initializeTables(){
        leftPartNumberColumn.setCellValueFactory(new PropertyValueFactory<>("part"));
        leftTableView.setItems(leftData);
        rightPartNumberColumn.setCellValueFactory(new PropertyValueFactory<>("part"));
        rightTableView.setItems(rightData);
    }

    //Convert to Factory?
    private void generateDefaultsIfMissing() {
        if(!fileExists("Wheeled_Coach.json")){
            TransmittalSettings WheeledCoach = TransmittalSettingsService.generateDefaultWheeledCoach();
            TransmittalSettingsService.save(WheeledCoach, "Wheeled_Coach.json");
        }
        if(!fileExists("Road_Rescue_EXT.json")){
            TransmittalSettings WheeledCoach = TransmittalSettingsService.generateDefaultRoadRescueExterior();
            TransmittalSettingsService.save(WheeledCoach, "Road_Rescue_EXT.json");
        }
        if(!fileExists("Road_Rescue_INT.json")){
            TransmittalSettings WheeledCoach = TransmittalSettingsService.generateDefaultRoadRescueInterior();
            TransmittalSettingsService.save(WheeledCoach, "Road_Rescue_INT.json");
        }
        if(!fileExists("Settings.json")){
            ApplicationSettings defaultSettings = ApplicationSettingsService.generateDefault();
            ApplicationSettingsService.save(defaultSettings, "Settings.json");
        }
    }
    
    private void loadDefaultSettings(){
        applicationSettings =
                ApplicationSettingsService.loadSettings(new File("." + File.separator + "Settings.json"));
        
        if(applicationSettings != null) {
            leftTransmittalSettings =
                    TransmittalSettingsService.loadSettings(new File("." + File.separator +
                            applicationSettings.getDefaultTransmittalLayout()));
            rightTransmittalSettings =
                    TransmittalSettingsService.loadSettings(new File("." + File.separator +
                            applicationSettings.getDefaultTransmittalLayout()));
        }
    }
    
    private void setupRowColors() {
        leftTableView.setRowFactory(tv -> new TableRow<>() {
            public void updateItem(TablePart tablePart, boolean empty) {
                super.updateItem(tablePart, empty);
                if (tablePart == null) {
                    setStyle("-fx-background-color: #303342");
                    return;
                }
                switch (tablePart.getStatus()) {
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
                if (tablePart == null) {
                    setStyle("-fx-background-color: #303342");
                    return;
                }
                switch (tablePart.getStatus()) {
                    case CORRECT -> setStyle("-fx-background-color: green");
                    case MISMATCH -> setStyle("-fx-background-color: yellow");
                    case MISSING -> setStyle("-fx-background-color: red");
                    case DEFAULT -> setStyle("-fx-background-color: gray");
                }
            }
        });
    }
    
    private boolean fileExists(String fileName){
        File file = new File("." + File.separator + fileName);
        return file.exists();
    }
    
    public void setHostServices(HostServices hostServices){
        this.hostServices = hostServices;
    }

    private File fileFromChooser() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Transmittal");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Excel Files", "*.xlsm"),
                new FileChooser.ExtensionFilter("All files", "*.*")
        );

        return fileChooser.showOpenDialog(null);
    }

}