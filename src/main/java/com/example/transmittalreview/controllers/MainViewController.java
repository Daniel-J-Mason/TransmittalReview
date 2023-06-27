package com.example.transmittalreview.controllers;

import com.example.transmittalreview.controllers.components.SettingsPopup;
import com.example.transmittalreview.controllers.components.TablePart;
import com.example.transmittalreview.model.dao.ApplicationSettings;
import com.example.transmittalreview.model.dao.TransmittalSettings;
import com.example.transmittalreview.model.entities.Drawing;
import com.example.transmittalreview.model.entities.Dxf;
import com.example.transmittalreview.model.entities.Part;
import com.example.transmittalreview.model.service.*;
import javafx.application.HostServices;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.css.PseudoClass;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.Clipboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Popup;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainViewController {
    public TableView<TablePart> leftTableView;
    public TableColumn<TablePart, String> leftPartNumberColumn;
    public TableView<TablePart> rightTableView;
    public TableColumn<TablePart, String> rightPartNumberColumn;
    public Button leftClipboard;
    public Button rightClipboard;
    public Button rightSelectFile;
    public Button leftSelectFile;
    public MenuItem settingsButton;
    public VBox mainBox;
    
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
        
        leftPartNumberColumn.setText("Clipboard");
        rightTableView.refresh(); // Update Color changes
    }
    
    public void rightClipboardClicked(ActionEvent actionEvent) {
        String clipboardData = clipboard.getString();
        rightBom = new BOMService(clipboardData).getParts();
        
        updateTableStatuses();
        
        rightPartNumberColumn.setText("Clipboard");
        leftTableView.refresh();// Update Color changes
    }
    
    public void leftSelectFileClicked(ActionEvent actionEvent) {
        File file = fileFromChooser();
        
        if (file == null) return;
        
        if (file.getName().endsWith(".xlsm")){
            leftBom = generateBomFromTransmittal(file, leftTransmittalSettings);
        } else {
            leftBom = generateBomFromBomFile(file);
        }
        
        leftPartNumberColumn.setText(file.getName());
        updateTableStatuses();
    }
    
    
    public void rightSelectFileClicked(ActionEvent actionEvent) {
        File file = fileFromChooser();
        
        if (file == null) return;
        
        if (file.getName().endsWith(".xlsm")){
            rightBom = generateBomFromTransmittal(file, rightTransmittalSettings);
        } else {
            rightBom = generateBomFromBomFile(file);
        }
        
        rightPartNumberColumn.setText(file.getName());
        updateTableStatuses();
    }
    
    private void initializeTables(){
        leftPartNumberColumn.setCellValueFactory(new PropertyValueFactory<>("part"));
        leftTableView.setItems(leftData);
        rightPartNumberColumn.setCellValueFactory(new PropertyValueFactory<>("part"));
        rightTableView.setItems(rightData);
    }
    
    //Convert to Factory?
    private void generateDefaultsIfMissing() {
        if(!settingsExists("Wheeled_Coach.json")){
            TransmittalSettings WheeledCoach = TransmittalSettingsService.generateDefaultWheeledCoach();
            TransmittalSettingsService.save(WheeledCoach, "Wheeled_Coach.json");
        }
        if(!settingsExists("Road_Rescue_EXT.json")){
            TransmittalSettings WheeledCoach = TransmittalSettingsService.generateDefaultRoadRescueExterior();
            TransmittalSettingsService.save(WheeledCoach, "Road_Rescue_EXT.json");
        }
        if(!settingsExists("Road_Rescue_INT.json")){
            TransmittalSettings WheeledCoach = TransmittalSettingsService.generateDefaultRoadRescueInterior();
            TransmittalSettingsService.save(WheeledCoach, "Road_Rescue_INT.json");
        }
        if(!settingsExists("Settings.json")){
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
            if (applicationSettings.isDarkMode()) {
                mainBox.getStylesheets().clear();
                mainBox.getStylesheets().add(getClass().getResource("styles/dark-theme.css").toExternalForm());
            } else {
                mainBox.getStylesheets().clear();
                mainBox.getStylesheets().add(getClass().getResource("styles/default-theme.css").toExternalForm());
            }
        }
    }
    
    private void setupRowColors() {
        leftTableView.setRowFactory(tv -> defaultRowStyle());
        rightTableView.setRowFactory(tv -> defaultRowStyle());
    }
    
    
    private List<Part> generateBomFromTransmittal(File transmittal, TransmittalSettings transmittalSettings){
        return TransmittalService.builder()
                .transmittalSettings(transmittalSettings)
                .build()
                .getParts(transmittal);
    }
    
    private List<Part> generateBomFromBomFile(File file){
        return new BOMService(file).getParts();
    }
    
    //Listener ILO?
    private void updateTableStatuses() {
        setLeftTableData();
        setRightTableData();
    }
    
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
    
    private TableRow<TablePart> defaultRowStyle(){
        return new TableRow<>() {
            public void updateItem(TablePart tablePart, boolean empty) {
                super.updateItem(tablePart, empty);
                // pseudo css class that can modify cell text based on fileExists
                PseudoClass missing = PseudoClass.getPseudoClass("missing-file");
                if (tablePart == null) {
                    setStyle("-fx-background-color: #303342; -fx-border-color: #303342");
                    return;
                } else {
                    Part part = tablePart.getPart();
                    File temp = new File(partLink(part));
                    pseudoClassStateChanged(missing, !temp.exists() && !part.isNew());
                    if(!part.isNew()) {
                        addEventHandler(MouseEvent.MOUSE_PRESSED, hyperLinkHandler(tablePart));
                    }
                }
                switch (tablePart.getStatus()) {
                    case CORRECT -> setStyle("-fx-background-color: green");
                    case MISMATCH -> setStyle("-fx-background-color: orange");
                    case MISSING -> setStyle("-fx-background-color: red");
                    case DEFAULT -> setStyle("-fx-background-color: gray");
                }
            }
        };
    }
    
    private EventHandler<MouseEvent> hyperLinkHandler(TablePart tablePart) {
        return event -> {
            if (event.isPrimaryButtonDown()) {
                hostServices.showDocument(partLink(tablePart.getPart()));
            } else if (event.isSecondaryButtonDown()) {
                File file = new File(partLink(tablePart.getPart()));
                hostServices.showDocument(file.getParent());
            }
        };
    }
    
    private String partLink(Part part){
        String link = "";
        String partFileName = part.getFileName();
        if (part instanceof Drawing) {
            String yearFolderName =
                    String.valueOf(applicationSettings.getDrawingPrefixes().get(Integer.parseInt(partFileName.substring(0, 2))));
            link = applicationSettings.getDrawingsFolder() + File.separator + yearFolderName + File.separator + partFileName;
        } else if (part instanceof Dxf){
            String yearFolderName =
                    String.valueOf(applicationSettings.getDxfPrefixes().get(Integer.parseInt(partFileName.substring(0, 2))));
            link = applicationSettings.getDxfFolder() + File.separator + yearFolderName + File.separator + partFileName;
        }
        return link;
    }
    
    private File fileFromChooser() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Transmittal");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Excel/BOM Files", "*.xlsm", "*.bom.*"),
                new FileChooser.ExtensionFilter("All files", "*.*")
        );
        
        return fileChooser.showOpenDialog(null);
    }
    
    
    private boolean settingsExists(String fileName){
        File file = new File("." + File.separator + fileName);
        return file.exists();
    }
    
    public void setHostServices(HostServices hostServices){
        this.hostServices = hostServices;
    }
    
    
    //TODO: Work on settings view
    public void settingsButtonClicked(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("settings.fxml"));
        Parent parent = fxmlLoader.load();
        SettingsViewController controller = fxmlLoader.getController();
        controller.setCallback(event -> {
            if (event.equalsIgnoreCase("SettingsChanged")){
                loadDefaultSettings();
            }
        });
        Scene scene = new Scene(parent);
        
        Stage dialog = new Stage();
        dialog.setTitle("Settings");
        dialog.setScene(scene);
        dialog.show();
    }
}