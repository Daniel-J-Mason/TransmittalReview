package com.example.transmittalreview.service;

import com.example.transmittalreview.dao.Settings;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;

public class SettingsService {
    private Settings settings;
    private String fileName;
    
    public SettingsService(Settings settings, String fileName){
        this.settings = settings;
        this.fileName = fileName;
    }
    
    public SettingsService(){
    }
    
    public Settings getSettings() {
        return settings;
    }
    
    public void setSettings(Settings settings) {
        this.settings = settings;
    }
    
    public String getFileName() {
        return fileName;
    }
    
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    
    //TODO: Edit for file fidelity
    public void save() throws IOException {
        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();
        Gson gson = builder.create();
        File currentDirectory = new File(".");
        FileWriter demo = new FileWriter(currentDirectory.getCanonicalFile() + File.separator + fileName + ".json");
        try {
            gson.toJson(settings, demo);
            demo.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
    
    public void loadSettings(File file){
        Gson gson = new Gson();
        FileReader reader = null;
        try {
            reader = new FileReader(file);
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
        if (reader == null){
            return;
        }
        settings = gson.fromJson(reader, Settings.class);
    }
}
