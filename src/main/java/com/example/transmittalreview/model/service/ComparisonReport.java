package com.example.transmittalreview.model.service;

import com.example.transmittalreview.model.entities.*;
import lombok.Data;

import java.util.*;

@Data
public class ComparisonReport {
    
    private ComparisonReport(){}
    
    public static Map<Part, Status> generateReport(List<Part> parts, List<Part> comparisonList){
        Map<Part, Status> result = new LinkedHashMap<>();
        for (Part part: parts){
            result.put(part, checkPart(part, comparisonList));
        }
        
        // Set BOM parts as New if they are correct and compared to a transmittal that reflects them as new
        for (Part part: result.keySet()){
            if (result.get(part) == Status.CORRECT) {
                Part comparisonPart = comparisonList.stream()
                        .filter(part1 -> part1.getPartNumber().equalsIgnoreCase(part.getPartNumber()))
                        .toList()
                        .get(0);
                if (comparisonPart.isNew()){
                    part.setIsNew(comparisonPart.isNew());
                }
            }
        }
        
        return result;
    }
    
    //
    private static Status checkPart(Part part, List<Part> compareList){
        Status result;
        if (part instanceof Drawing){
            result = checkDrawing(part, compareList);
        } else {
            result = checkDxf(part, compareList);
        }
        
        return result;
    }
    
    
    // if theyre different part types, dont check rev level, if they are check both
    private static Status checkDrawing(Part part, List<Part> compareList){
        Status result = Status.MISSING;
        for (Part comparisonPart: compareList){
            if (comparisonPart.getPartNumber().equalsIgnoreCase(part.getPartNumber())){
                if (comparisonPart instanceof Dxf && !((Dxf) comparisonPart).isSkin()){
                    result = Status.CORRECT;
                } else if (comparisonPart instanceof Drawing){
                    if (equalsIgnoreCaseNullCase(comparisonPart.getRevisionLevel(), part.getRevisionLevel())){
                        result = Status.CORRECT;
                    } else {
                        result = Status.MISMATCH;
                    }
                }
            }
        }
        
        return result;
    }
    
    // if theyre different part types, dont check rev level, if they are check both
    private static Status checkDxf(Part part, List<Part> compareList){
        Status result = Status.MISSING;
        for (Part comparisonPart: compareList){
            if (comparisonPart.getPartNumber().equalsIgnoreCase(part.getPartNumber())){
                if (comparisonPart instanceof Drawing){
                    result = Status.CORRECT;
                } else if (comparisonPart instanceof Dxf && !((Dxf) comparisonPart).isSkin()){
                    if (equalsIgnoreCaseNullCase(comparisonPart.getRevisionLevel(), part.getRevisionLevel())){
                        result = Status.CORRECT;
                    } else {
                        result = Status.MISMATCH;
                    }
                }
            }
        }
        
        return result;
    }
    
    private static boolean equalsIgnoreCaseNullCase(String first, String second){
        if (first == null && second == null){
            return true;
        } else if (first == null || second == null){
            return false;
        } else {
            return first.equalsIgnoreCase(second);
        }
    }
}
