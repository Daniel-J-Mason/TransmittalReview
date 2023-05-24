package com.example.transmittalreview.model.service;

import com.example.transmittalreview.model.entities.BOM;
import com.example.transmittalreview.model.entities.Drawing;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//TODO rewrite so bom is no longer a variable, String input is the variable extractBom renamed to getBom.
public class BOMService {
    private BOM bom;
    
    public BOMService(String clipboardInput){
        extractBom(clipboardInput);
    }
    
    public BOMService(File fileInput){
        String content = fileToString(fileInput);
        extractBom(content);
    }
    
    public BOM getBom() {
        return bom;
    }
    
    
    private void extractBom(String input){
        List<Drawing> result = new ArrayList<>();
        //Group 0 full, Group 1 / 5 Strip _ for name, Group 3 is for optional, Group 4 rev level
        Pattern pattern = Pattern.compile("(\\w*)?(\\d{6}[Dd])(_rev_(\\w))?(\\w*)?", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(input);
        while (matcher.find()) {
            result.add(parseSingleDrawing(matcher.group()));
        }
        this.bom = new BOM(result);
    }
    
    private String fileToString(File inputFile){
        BufferedReader reader = null;
        StringBuilder stringBuilder = new StringBuilder();
        try {
            reader = new BufferedReader(new FileReader(inputFile.getCanonicalFile()));
            String line = reader.readLine();
            
            int frontCounter = 0;
            while (line != null && frontCounter < 2){
                if (line.contains("BFTA")){
                    frontCounter++;
                }
                stringBuilder.append(line).append("\\n");
                line = reader.readLine();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    
        return stringBuilder.toString();
    }
    
    private Drawing parseSingleDrawing(String input){
        Pattern pattern = Pattern.compile("(\\w*)?(\\d{6}[Dd])(_rev_(\\w))?(\\w*)?", Pattern.CASE_INSENSITIVE);
        Matcher drawingPattern = pattern.matcher(input);
        drawingPattern.find();
        String fullname = drawingPattern.group(0);
        String name = null;
        String partNumber = drawingPattern.group(2);
        if (drawingPattern.group(1) == null || drawingPattern.group(1).equals("")){
            name = drawingPattern.group(5).replaceAll("_", " ").strip();
        } else {
            name = drawingPattern.group(1).replaceAll("_"," ").strip();
        }
        String revisionLevel = null;
        if (drawingPattern.group(4) == null){
            revisionLevel = "0";
        } else {
            revisionLevel = drawingPattern.group(4);
        }
        String filepath = "";
        
        return new Drawing(fullname, name, partNumber, revisionLevel, filepath);
    }
}
