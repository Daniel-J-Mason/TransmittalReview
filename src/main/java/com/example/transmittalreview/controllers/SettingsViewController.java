package com.example.transmittalreview.controllers;

import com.example.transmittalreview.model.dao.ApplicationSettings;
import com.example.transmittalreview.model.service.ApplicationSettingsService;
import com.example.transmittalreview.model.service.TransmittalSettingsService;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class SettingsViewController {
    public Consumer<String> demoConsumer;
    public VBox settingsRoot;
    public TextField drawingsFolderField;
    public Button drawingsFolderButton;
    public TextField dxfFolderField;
    public Button dxfFolderButton;
    public MenuButton layoutMenuButton;
    public CheckBox darkModeToggle;
    public Button applyButton;
    public Button cancelButton;
    
    private ApplicationSettings applicationSettings;
    
    public void initialize(){
        loadDefaultSettings();
        setUpMenuBar();
    }
    
    public void applyButtonPressed(ActionEvent actionEvent) {
        if (demoConsumer != null){
            saveSettings();
            demoConsumer.accept("SettingsChanged");
            closeWindow();
        }
    }
    
    public void drawingsFolderButtonClicked(ActionEvent actionEvent) {
        DirectoryChooser drawingsFolder = new DirectoryChooser();
        drawingsFolder.setTitle("Select Drawings Folder");
        
        File file = drawingsFolder.showDialog(null);
        
        if (file != null) {
            drawingsFolderField.setText(file.getAbsolutePath());
        }
    }
    
    public void dxfFolderButtonClicked(ActionEvent actionEvent) {
        DirectoryChooser dxfFolder = new DirectoryChooser();
        dxfFolder.setTitle("Select Drawings Folder");
        
        File file = dxfFolder.showDialog(null);
        
        if (file != null) {
            dxfFolderField.setText(file.getAbsolutePath());
        }
    }
    
    private void loadDefaultSettings(){
        applicationSettings =
                ApplicationSettingsService.loadSettings(new File("." + File.separator + "Settings.json"));
        if(applicationSettings != null) {
            if (applicationSettings.isDarkMode()) {
                settingsRoot.getStylesheets().clear();
                settingsRoot.getStylesheets().add(getClass().getResource("styles/dark-theme.css").toExternalForm());
            } else {
                settingsRoot.getStylesheets().clear();
                settingsRoot.getStylesheets().add(getClass().getResource("styles/light-theme.css").toExternalForm());
            }
            drawingsFolderField.setText(applicationSettings.getDrawingsFolder());
            dxfFolderField.setText(applicationSettings.getDxfFolder());
            layoutMenuButton.setText(applicationSettings.getDefaultTransmittalLayout());
            darkModeToggle.setSelected(applicationSettings.isDarkMode());
        }
    }
    
    private void setUpMenuBar(){
        layoutMenuButton.setText(applicationSettings.getDefaultTransmittalLayout());
        
        layoutMenuButton.getItems().addAll(localTransmittalLayouts(layoutMenuButton));
    }
    
    private List<MenuItem> localTransmittalLayouts(MenuButton belongsTo){
        File current = new File(".");
        return Arrays.stream(current.listFiles())
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
        return menuItem;
    }
    
    public void cancelButtonClicked(ActionEvent actionEvent) {
        closeWindow();
    }
    
    private void saveSettings(){
        applicationSettings.setDrawingsFolder(drawingsFolderField.getText());
        applicationSettings.setDxfFolder(dxfFolderField.getText());
        applicationSettings.setDefaultTransmittalLayout(layoutMenuButton.getText());
        applicationSettings.setDarkMode(darkModeToggle.selectedProperty().get());
        ApplicationSettingsService.save(applicationSettings, "Settings.json");
    }
    
    public void setCallback(Consumer<String> demo){
        this.demoConsumer = demo;
    }
    
    private void closeWindow(){
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }
}
