package com.example.transmittalreview.model.service;

import com.example.transmittalreview.model.entities.Drawing;
import com.example.transmittalreview.model.entities.Part;
import lombok.Data;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Data
public class BOMService {
    private String content;
    
    public BOMService(String clipboardInput){
        content = clipboardInput;
    }
    
    public BOMService(File fileInput){
        content = fileToContent(fileInput);
    }
    
    public List<Part> getParts() {
        List<Part> result = new ArrayList<>();
        
        Matcher matcher = drawingMatcher(content);
        while (matcher.find()) {
            result.add(parseSingleDrawing(matcher.group()));
        }
        
        return result;
    }
    
    private String fileToContent(File inputFile){
        BufferedReader reader = null;
        StringBuilder contentBuilder = new StringBuilder();
        try {
            reader = new BufferedReader(new FileReader(inputFile.getCanonicalFile()));
            String line = reader.readLine();
            
            //Uses BFTA as a marker to pull just the top level BOM
            int frontAssemblyCounter = 0;
            while (line != null && frontAssemblyCounter < 2){
                if (line.contains("BFTA")){
                    frontAssemblyCounter++;
                }
                contentBuilder.append(line).append("\\n");
                line = reader.readLine();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    
        return contentBuilder.toString();
    }
    
    private Drawing parseSingleDrawing(String input){
        Matcher singleDrawing = drawingMatcher(input);
        if (singleDrawing.find()) {
            return Drawing.builder()
                    //if prefix is null, check suffix, if suffix is also null return null.
                    .prefix(singleDrawing.group(1) != null ? singleDrawing.group(1) :
                            singleDrawing.group(5) != null ? singleDrawing.group(5) : null)
                    .partNumber(singleDrawing.group(2))
                    .revisionLevel(singleDrawing.group(4))
                    .build();
        } else {
            return null;
        }
    }
    
    private Matcher drawingMatcher(String textToSearch){
        //Group 0 full, Group 1 / 5 Strip _ for name, Group 3 is for optional, Group 4 rev level
        Pattern pattern = Pattern.compile("(\\w*)?_?(\\d{6}[Dd])(_rev_(\\w))?_?(\\w*)?", Pattern.CASE_INSENSITIVE);
        return pattern.matcher(textToSearch);
    }
}
