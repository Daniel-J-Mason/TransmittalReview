package com.example.transmittalreview.model.service;

import com.example.transmittalreview.model.entities.*;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;


public class ComparisonReport {
    private BOM original;
    private BOM comparison;
    
    public ComparisonReport(BOM original, BOM comparison){
        this.original = original;
        this.comparison = comparison;
    }
    
    public List<Part> report(){
        List<Part> result = new ArrayList<>(original.getDrawings());
        result.addAll(original.getDxfs());
        
        result.forEach(this::flagPart);
        
        return result;
    }
    
    //
    private void flagPart(Part part){
        if (part instanceof Drawing){
            flagDrawing((Drawing) part);
        } else {
            flagTextFile((Dxf) part);
        }
    }
    
    //TODO drawing check to drawing and revision Green, then drawing Orange, then dxf Green, Uptodate flag checkbox table
    //TODO Up to date should be separate from green orange and red, substitute for yellow
    private void flagDrawing(Drawing drawing){
        List<Pair<String, String>> numberAndRevision = new ArrayList<>();
        comparison.getDrawings().forEach(element -> numberAndRevision.add(new Pair<>(element.getPartNumber(), element.getRevisionLevel())));
        for (Pair<String, String> pair: numberAndRevision){
            if (drawing.getPartNumber().equalsIgnoreCase(pair.getKey())){
                if (drawing.getRevisionLevel().equalsIgnoreCase(pair.getValue())){
                    drawing.setStatus(Status.CORRECT);
                    return;
                }
                drawing.setStatus(Status.MISMATCH);
                return;
            }
        }
        List<String> textFiles = comparison.getDxfs().stream().map(Part::getPartNumber).toList();
        if (textFiles.contains(drawing.getPartNumber())){
            drawing.setStatus(Status.CORRECT);
        }
        
        drawing.setStatus(Status.MISSING);
    }
    
    private void flagTextFile(Dxf dxf){
        if (comparison.getDxfs().isEmpty()){
            List<String> parts = comparison.getDrawings().stream().map(Part::getPartNumber).toList();
            if (parts.contains(dxf.getPartNumber())){
                dxf.setStatus(Status.CORRECT);
                return;
            }
        } else {
            List<Pair<String, String>> numberAndRevision = comparison
                    .getDxfs()
                    .stream()
                    .map(element -> new Pair<>(element.getDxfNumber(), element.getRevisionLevel()))
                    .toList();
            for (Pair<String, String> pair: numberAndRevision){
                if (dxf.getDxfNumber().equalsIgnoreCase(pair.getKey())){
                    if (dxf.getRevisionLevel().equalsIgnoreCase(pair.getValue())){
                        dxf.setStatus(Status.CORRECT);
                    } else {
                        dxf.setStatus(Status.MISMATCH);
                    }
                    return;
                }
            }
        }
        dxf.setStatus(Status.MISSING);
    }
}
