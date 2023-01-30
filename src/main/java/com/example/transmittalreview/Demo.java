package com.example.transmittalreview;

import com.example.transmittalreview.dao.Settings;
import com.example.transmittalreview.entities.TransmittalPageLayout;
import com.example.transmittalreview.service.BOMService;
import com.example.transmittalreview.service.SettingsService;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TreeMap;

public class Demo {
    public static void main(String[] args) throws IOException {
        Settings wheeledCoach = new Settings();
        wheeledCoach.setEngineeringWorkingFolder("EngineeringWorking");
        wheeledCoach.setDrawingsFolder("drawingsFolder");
        wheeledCoach.setDxfFolder("dxfFolder");
        
        TransmittalPageLayout pageOne = new TransmittalPageLayout();
        pageOne.setPageName("BS Transmittal 1");
        pageOne.setDrawingRows(new ArrayList<>(Arrays.asList(9, 11, 13, 14 , 15, 16, 17, 18, 19, 20, 22, 23, 24, 25, 26, 27, 28, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 41, 42, 43, 44, 45, 46, 47)));
        pageOne.setTextRows(new ArrayList<>(Arrays.asList(10, 12, 21, 29)));
        pageOne.setNameColumn(1);
        pageOne.setNumberColumn(2);
        pageOne.setRevisionColumn(3);
        TransmittalPageLayout pageTwo = new TransmittalPageLayout();
        pageTwo.setPageName("BS Transmittal 2");
        pageTwo.setDrawingRows(new ArrayList<>(Arrays.asList(11, 12, 13, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41)));
        pageTwo.setTextRows(new ArrayList<>(Arrays.asList(14, 15, 16)));
        pageTwo.setNameColumn(1);
        pageTwo.setNumberColumn(2);
        pageTwo.setRevisionColumn(4);
        wheeledCoach.setTransmittalLayout(new ArrayList<>(Arrays.asList(pageOne,pageTwo)));
        TreeMap<String, String> drawingPrefixes = new TreeMap<>();
        ArrayList<String> contructor = new ArrayList<>();
        for (int i = 95; i <= 99; i++){
            drawingPrefixes.put(String.valueOf(i), String.valueOf(i+1901));
        }
        for (int i = 20; i <= 29; i++){
            drawingPrefixes.put(String.valueOf(i), String.valueOf(i+1980));
        }
        for (int i = 30; i <= 60; i++){
            drawingPrefixes.put(String.valueOf(i), String.valueOf(i+1980));
        }
        wheeledCoach.setDrawingPrefixes(drawingPrefixes);
        wheeledCoach.setDxfPrefixes(drawingPrefixes);
        SettingsService service = new SettingsService(wheeledCoach, "Wheeled_Coach");
        service.save();
//        wheeledCoach.setTransmittalLayout(new ArrayList<>(List.of(pageOne)));
//        HashMap<String, String> test = new HashMap<>();
//        test.put("test", "test");
//        wheeledCoach.setDrawingPrefixes(test);
//        wheeledCoach.setDxfPrefixes(test);
//
//        SettingsService service = new SettingsService(wheeledCoach, "wheeledCoach");
//        service.save();

//        Gson gson = new Gson();
//        ArrayList<Integer> demo = new ArrayList<>(Arrays.asList(1, 2, 3));
//        System.out.println(gson.toJson(wheeledCoach));
//
//        File testFile = new File("/home/kal/ReferenceHub/TransmittalReviewDocs/0000_579306_gsa_comsupcen.bom.1");
//        BOMService testService = new BOMService(testFile);
//        testService.getParts().forEach(x -> System.out.printf("Name: %s, Part Number: %s, Revision level: %s%n",
//                x.getName(), x.getPartNumber(), x.getRevisionLevel()));
//
    }
}
