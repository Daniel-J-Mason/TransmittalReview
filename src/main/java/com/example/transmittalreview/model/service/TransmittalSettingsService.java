package com.example.transmittalreview.model.service;

import com.example.transmittalreview.model.dao.TransmittalSettings;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TransmittalSettingsService {
    
    public static void save(TransmittalSettings transmittalSettings, String fileName) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder
                .setPrettyPrinting()
                .create();
        
        try {
            FileWriter fileWriter =
                    new FileWriter("." + File.separator + fileName);
            gson.toJson(transmittalSettings, fileWriter);
            fileWriter.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
    
    public static TransmittalSettings loadSettings(File file){
        Gson gson = new Gson();
        FileReader reader = null;
        try {
            reader = new FileReader(file);
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
        
        return reader == null ? null : gson.fromJson(reader, TransmittalSettings.class);
    }
    
    public static TransmittalSettings generateDefaultWheeledCoach(){
        List<TransmittalSettings.PageLayout> pageLayouts = new ArrayList<>();
        
        List<Integer> firstPageDrawingList = 
                Arrays.asList(9, 11, 13, 14, 15, 16, 17, 18, 19, 20, 22, 23, 24, 25, 26, 27, 28, 30, 31, 32, 33, 34, 35,
                        36, 37, 38, 39, 41, 42, 43, 44, 45, 46, 47);
        List<Integer> firstPageSkinList =
                Arrays.asList(10, 12, 21, 29);
        
        pageLayouts.add(
                TransmittalSettings.PageLayout.builder()
                        .pageName("BS Transmittal 1")
                        .drawingRows(firstPageDrawingList)
                        .textRows(new ArrayList<>())
                        .skinRows(firstPageSkinList)
                        .nameColumn(1)
                        .numberColumn(2)
                        .revisionColumn(4)
                        .build());
        
        List<Integer> secondPageDrawingList =
                Arrays.asList(11, 12, 13, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 30, 31, 32, 33, 34, 35, 36, 37,
                        38, 39, 40, 41);
        List<Integer> secondPageDxfList =
                Arrays.asList(14, 15, 16);
        
        pageLayouts.add(
                TransmittalSettings.PageLayout.builder()
                        .pageName("BS Transmittal 2")
                        .drawingRows(secondPageDrawingList)
                        .textRows(secondPageDxfList)
                        .skinRows(new ArrayList<>())
                        .nameColumn(1)
                        .numberColumn(2)
                        .revisionColumn(4)
                        .build());
        
        return TransmittalSettings.builder()
                .pageLayouts(pageLayouts)
                .build();
    }
    
    public static TransmittalSettings generateDefaultRoadRescueExterior(){
        List<TransmittalSettings.PageLayout> pageLayouts = new ArrayList<>();
        
        List<Integer> firstPageDrawingList =
                Arrays.asList(9, 11, 12, 14, 15, 16, 17, 18, 19, 20, 21, 22, 24, 25, 26, 27, 28, 31, 32, 33, 34, 35, 36
                , 41, 42, 43, 44, 45, 46, 47);
        List<Integer> firstPageDxfList =
                Arrays.asList(30, 37, 38, 39, 40);
        List<Integer> firstPageSkins =
                Arrays.asList(10, 13, 23, 29);
        
        pageLayouts.add(
                TransmittalSettings.PageLayout.builder()
                        .pageName("BODY SHOP TRANSMITTAL 1")
                        .drawingRows(firstPageDrawingList)
                        .textRows(firstPageDxfList)
                        .skinRows(firstPageSkins)
                        .nameColumn(1)
                        .numberColumn(2)
                        .revisionColumn(4)
                        .build());
        
        List<Integer> secondPageDrawingList =
                Arrays.asList(12, 15, 16, 17, 18, 20, 21, 22, 23, 24, 25, 26, 27, 28, 30, 31, 32, 33, 34, 35, 36, 37,
                        38, 39, 40, 41, 42, 43);
        List<Integer> secondPageDxfList =
                Arrays.asList(10, 11);
        
        pageLayouts.add(
                TransmittalSettings.PageLayout.builder()
                        .pageName("BODY SHOP TRANSMITTAL 2")
                        .drawingRows(secondPageDrawingList)
                        .textRows(secondPageDxfList)
                        .skinRows(new ArrayList<>())
                        .nameColumn(1)
                        .numberColumn(2)
                        .revisionColumn(4)
                        .build());
        
        return TransmittalSettings.builder()
                .pageLayouts(pageLayouts)
                .build();
    }
    
    public static TransmittalSettings generateDefaultRoadRescueInterior(){
        List<TransmittalSettings.PageLayout> pageLayouts = new ArrayList<>();
        
        List<Integer> firstPageDrawingList =
                Arrays.asList(10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 26, 27, 28, 29, 30, 31, 32,
                        33, 34, 35, 36, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 49, 50, 51, 52, 53, 54, 58, 59, 60, 61,
                        62, 63);
        List<Integer> firstPageDxfList =
                Arrays.asList(56, 57);
        
        pageLayouts.add(
                TransmittalSettings.PageLayout.builder()
                        .pageName("CABINET SHOP TRANSMITTAL")
                        .drawingRows(firstPageDrawingList)
                        .textRows(firstPageDxfList)
                        .skinRows(new ArrayList<>())
                        .nameColumn(1)
                        .numberColumn(2)
                        .revisionColumn(4)
                        .build());
        
        List<Integer> secondPageDrawingList =
                Arrays.asList(10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31,
                        32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44);
        List<Integer> secondPageDxfList = new ArrayList<>();
        
        pageLayouts.add(
                TransmittalSettings.PageLayout.builder()
                        .pageName("MISC CABINET PIECES")
                        .drawingRows(secondPageDrawingList)
                        .textRows(secondPageDxfList)
                        .skinRows(new ArrayList<>())
                        .nameColumn(1)
                        .numberColumn(2)
                        .revisionColumn(4)
                        .build());
        
        return TransmittalSettings.builder()
                .pageLayouts(pageLayouts)
                .build();
    }
}
