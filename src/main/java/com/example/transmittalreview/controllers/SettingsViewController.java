package com.example.transmittalreview.controllers;

import com.example.transmittalreview.model.dao.ApplicationSettings;
import com.example.transmittalreview.model.service.ApplicationSettingsService;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.function.Consumer;

public class SettingsViewController {
    public Button demoButton;
    
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
    
    
    //TODO: finish loadDefaultSettings setText on all fields
    private ApplicationSettings applicationSettings;
    
    public void initialize(){
        loadDefaultSettings();
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
                settingsRoot.getStylesheets().add(getClass().getResource("styles/default-theme.css").toExternalForm());
            }
            drawingsFolderField.setText(applicationSettings.getDrawingsFolder());
            dxfFolderField.setText(applicationSettings.getDxfFolder());
            layoutMenuButton.setText(applicationSettings.getDefaultTransmittalLayout());
            darkModeToggle.setSelected(applicationSettings.isDarkMode());
        }
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
    
    public void cancelButtonClicked(ActionEvent actionEvent) {
        closeWindow();
    }
    
    private void closeWindow(){
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }
}
