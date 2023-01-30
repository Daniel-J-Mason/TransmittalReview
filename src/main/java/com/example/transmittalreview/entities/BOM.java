package com.example.transmittalreview.entities;

import java.util.ArrayList;
import java.util.List;

public class BOM {
    private List<Drawing> parts;
    private List<Dxf> textFiles;
    
    public BOM(List<Drawing> parts, List<Dxf> textFiles){
        this.parts = new ArrayList<>(parts);
        this.textFiles = new ArrayList<>(textFiles);
    }
    
    public BOM(List<Drawing> parts){
        this.parts = new ArrayList<>(parts);
        this.textFiles = new ArrayList<>();
    }
    
    public BOM(){
        this.parts = new ArrayList<>();
        this.textFiles = new ArrayList<>();
    }
    
    public List<Drawing> getParts() {
        return new ArrayList<>(parts);
    }
    
    public void setParts(List<Drawing> parts) {
        this.parts = new ArrayList<>(parts);
    }
    
    public List<Dxf> getTextFiles() {
        return new ArrayList<>(textFiles);
    }
    
    public void setTextFiles(List<Dxf> textFiles) {
        this.textFiles = new ArrayList<>(textFiles);
    }
}
