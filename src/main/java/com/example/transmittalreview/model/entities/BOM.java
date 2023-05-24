package com.example.transmittalreview.model.entities;

import java.util.ArrayList;
import java.util.List;

public class BOM {
    private List<Drawing> drawings;
    private List<Dxf> dxfs;
    
    public BOM(List<Drawing> drawings, List<Dxf> dxfs){
        this.drawings = new ArrayList<>(drawings);
        this.dxfs = new ArrayList<>(dxfs);
    }
    
    public BOM(List<Drawing> drawings){
        this.drawings = new ArrayList<>(drawings);
        this.dxfs = new ArrayList<>();
    }
    
    public BOM(){
        this.drawings = new ArrayList<>();
        this.dxfs = new ArrayList<>();
    }
    
    public List<Drawing> getDrawings() {
        return new ArrayList<>(drawings);
    }
    
    public void setDrawings(List<Drawing> drawings) {
        this.drawings = new ArrayList<>(drawings);
    }
    
    public List<Dxf> getDxfs() {
        return new ArrayList<>(dxfs);
    }
    
    public void setDxfs(List<Dxf> dxfs) {
        this.dxfs = new ArrayList<>(dxfs);
    }
    
    public Drawing getPartByNumber(String partNumber){
        for (Drawing drawing: drawings){
            if (drawing.getPartNumber().equalsIgnoreCase(partNumber)){
                return drawing;
            }
        }
        
        return null;
    }
    
    public Dxf getDxfByNumber(String dxfNumber){
        for (Dxf dxf: dxfs){
            if (dxf.getPartNumber().equalsIgnoreCase(dxfNumber)){
                return dxf;
            }
        }
    
        return null;
    }
}
