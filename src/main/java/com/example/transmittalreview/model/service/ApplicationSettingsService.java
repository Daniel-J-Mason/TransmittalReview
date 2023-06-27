package com.example.transmittalreview.model.service;

import com.example.transmittalreview.model.dao.ApplicationSettings;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Data;

import java.io.*;
import java.util.TreeMap;

@Data
public class ApplicationSettingsService {
    public static void save(ApplicationSettings applicationSettings, String fileName) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder
                .setPrettyPrinting()
                .create();
        
        try {
            FileWriter fileWriter = new FileWriter("." + File.separator + fileName);
            gson.toJson(applicationSettings, fileWriter);
            fileWriter.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
    
    public static ApplicationSettings loadSettings(File file){
        Gson gson = new Gson();
        FileReader reader = null;
        try {
            reader = new FileReader(file);
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
        
        return reader == null ? null : gson.fromJson(reader, ApplicationSettings.class);
    }
    
    public static ApplicationSettings generateDefault(){

        TreeMap<Integer, Integer> prefixes = new TreeMap<>();
        
        for (int prefix = 0; prefix <= 60; prefix++){
            int year = prefix + 1980;
            prefixes.put(prefix, year);
        }
        
        for (int oldPrefix = 95; oldPrefix <= 99; oldPrefix++){
            int year = oldPrefix + 1900;
            prefixes.put(oldPrefix, year);
        }
        
        return ApplicationSettings.builder()
                .engineeringWorkingFolder("engineeringWorkingFolder")
                .drawingsFolder("drawingsFolder")
                .dxfFolder("dxfFolder")
                .drawingPrefixes(prefixes)
                .dxfPrefixes(prefixes)
                .defaultTransmittalLayout("Wheeled_Coach.json")
                .isDarkMode(true)
                .helpFolder("helpFolder")
                .build();
    }
}
