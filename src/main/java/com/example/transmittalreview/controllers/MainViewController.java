package com.example.transmittalreview.controllers;

import com.example.transmittalreview.controllers.components.TablePart;
import com.example.transmittalreview.model.dao.ApplicationSettings;
import com.example.transmittalreview.model.dao.TransmittalSettings;
import com.example.transmittalreview.model.entities.Drawing;
import com.example.transmittalreview.model.entities.Dxf;
import com.example.transmittalreview.model.entities.Part;
import com.example.transmittalreview.model.service.ApplicationSettingsService;
import com.example.transmittalreview.model.service.BOMService;
import com.example.transmittalreview.model.service.ComparisonReport;
import com.example.transmittalreview.model.service.TransmittalService;
import com.example.transmittalreview.model.service.TransmittalSettingsService;
import javafx.application.HostServices;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.css.PseudoClass;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.Clipboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

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
    public MenuButton leftDefaultLayoutMenu;
    public MenuButton rightDefaultLayoutMenu;

    private ApplicationSettings applicationSettings;
    private TransmittalSettings leftTransmittalSettings;
    private TransmittalSettings rightTransmittalSettings;
    private List<Part> leftBom = new ArrayList<>();
    private List<Part> rightBom = new ArrayList<>();
    private final ObservableList<TablePart> leftData = FXCollections.observableArrayList();
    private final ObservableList<TablePart> rightData = FXCollections.observableArrayList();
    private final Clipboard clipboard = Clipboard.getSystemClipboard();
    private HostServices hostServices;
    private String lastFolder;
    
    public void initialize() {
        initializeTables();
        generateDefaultsIfMissing();
        loadDefaultSettings();
        setupRowColors();
        setUpMenuBars();
    }

    public void aboutButtonClicked() {
        hostServices.showDocument(applicationSettings.getHelpFolder());
    }

    public void leftClipboardClicked() {
        String clipboardData = clipboard.getString();
        leftBom = new BOMService(clipboardData).getParts();
        
        updateTableStatuses();
        
        leftPartNumberColumn.setText("Clipboard");
    }
    
    public void rightClipboardClicked() {
        String clipboardData = clipboard.getString();
        rightBom = new BOMService(clipboardData).getParts();
        
        updateTableStatuses();
        
        rightPartNumberColumn.setText("Clipboard");
    }
    
    public void leftSelectFileClicked() {
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
    
    
    public void rightSelectFileClicked() {
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
    
    public void clearLeftData() {
        leftBom.clear();
        leftPartNumberColumn.setText("Part Number");
        updateTableStatuses();
    }
    
    public void clearRightData() {
        rightBom.clear();
        rightPartNumberColumn.setText("Part Number");
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
                mainBox.getStylesheets().add(Objects.requireNonNull(getClass().getResource("styles/dark-theme.css")).toExternalForm());
            } else {
                mainBox.getStylesheets().clear();
                mainBox.getStylesheets().add(Objects.requireNonNull(getClass().getResource("styles/light-theme.css")).toExternalForm());
            }
        }
    }
    
    private void setupRowColors() {
        leftTableView.setRowFactory(tv -> defaultRowStyle());
        rightTableView.setRowFactory(tv -> defaultRowStyle());
    }
    
    private void setUpMenuBars(){
        leftDefaultLayoutMenu.setText(applicationSettings.getDefaultTransmittalLayout());
        rightDefaultLayoutMenu.setText(applicationSettings.getDefaultTransmittalLayout());

        leftDefaultLayoutMenu.getItems().clear();
        rightDefaultLayoutMenu.getItems().clear();

        leftDefaultLayoutMenu.getItems().addAll(localTransmittalLayouts(leftDefaultLayoutMenu));
        rightDefaultLayoutMenu.getItems().addAll(localTransmittalLayouts(rightDefaultLayoutMenu));
        
        leftDefaultLayoutMenu.textProperty()
                .addListener((observableValue, oldValue, newValue) -> leftTransmittalSettings =
                TransmittalSettingsService.loadSettings(new File("." + File.separator + newValue)));
        
        rightDefaultLayoutMenu.textProperty()
                .addListener((observableValue, oldValue, newValue) -> rightTransmittalSettings =
                TransmittalSettingsService.loadSettings(new File("." + File.separator + newValue)));
    }
    
    private List<MenuItem> localTransmittalLayouts(MenuButton belongsTo){
        File current = new File(".");
        return Arrays.stream(Objects.requireNonNull(current.listFiles()))
                .filter(
                        element -> element.getName().endsWith(".json")
                                && !element.getName().equalsIgnoreCase("settings.json"))
                .map(element->layoutMenuButton(element.getName(), belongsTo))
                .toList();
    }
    
    private MenuItem layoutMenuButton(String text, MenuButton belongsTo){
        MenuItem menuItem = new MenuItem(text);
        menuItem.setOnAction(
                actionEvent -> belongsTo.setText(text)
        );
        menuItem.setMnemonicParsing(false);
        return menuItem;
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
                    if (!part.isNew()){
                        setEventHandler(MouseEvent.MOUSE_PRESSED, hyperLinkHandler(tablePart));
                    } else {
                        setEventHandler(MouseEvent.MOUSE_PRESSED, null);
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
                System.out.println("Secondary");
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
        fileChooser.setTitle("Select Transmittal or BOM file");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Excel/BOM Files", "*.xlsm", "*.bom.*"),
                new FileChooser.ExtensionFilter("All files", "*.*")
        );
        if (lastFolder != null){
            fileChooser.setInitialDirectory(new File(lastFolder));
        }

        File resultFile = fileChooser.showOpenDialog(null);

        if (resultFile != null) {
            lastFolder = resultFile.getAbsoluteFile().getParentFile().getAbsolutePath();
            System.out.println(lastFolder);
        }
        return resultFile;
    }
    
    
    private boolean settingsExists(String fileName){
        File file = new File("." + File.separator + fileName);
        return file.exists();
    }
    
    public void setHostServices(HostServices hostServices){
        this.hostServices = hostServices;
    }
    
    public void settingsButtonClicked() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("settings.fxml"));
        Parent parent = fxmlLoader.load();
        SettingsViewController controller = fxmlLoader.getController();
        controller.setCallback(event -> {
            if (event.equalsIgnoreCase("SettingsChanged")){
                loadDefaultSettings();
                setUpMenuBars();
            }
        });
        Scene scene = new Scene(parent);
        
        Stage dialog = new Stage();
        dialog.setTitle("Settings");
        dialog.setScene(scene);
        dialog.getIcons().add(
                new Image(Objects.requireNonNull(
                        SettingsViewController.class.getResourceAsStream("images/Transmittal-logo-128.png"))));
        dialog.show();
    }
}