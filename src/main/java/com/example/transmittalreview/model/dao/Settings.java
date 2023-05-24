package com.example.transmittalreview.model.dao;

import java.util.ArrayList;
import java.util.TreeMap;
import java.util.List;
//TODO: Split transmittal settings and configuration settings into two files. One for comparison one for app
public class Settings {
    private String engineeringWorkingFolder;
    private String drawingsFolder;
    private String dxfFolder;
    private TreeMap<String, String> drawingPrefixes;
    private TreeMap<String, String> dxfPrefixes;
    private List<TransmittalPageLayout> transmittalLayout;
    
    public Settings(){
        drawingPrefixes = new TreeMap<>();
        dxfPrefixes = new TreeMap<>();
        transmittalLayout = new ArrayList<>();
    }
    
    public Settings(String engineeringWorkingFolder, String drawingsFolder, String dxfFolder,
                    TreeMap<String, String> drawingPrefixes, TreeMap<String, String> dxfPrefixes,
                    List<TransmittalPageLayout> transmittalLayout) {
        this.engineeringWorkingFolder = engineeringWorkingFolder;
        this.drawingsFolder = drawingsFolder;
        this.dxfFolder = dxfFolder;
        this.drawingPrefixes = new TreeMap<>(drawingPrefixes);
        this.dxfPrefixes = new TreeMap<>(dxfPrefixes);
        this.transmittalLayout = new ArrayList<>(transmittalLayout);
    }
    
    public String getEngineeringWorkingFolder() {
        return engineeringWorkingFolder;
    }
    
    public void setEngineeringWorkingFolder(String engineeringWorkingFolder) {
        this.engineeringWorkingFolder = engineeringWorkingFolder;
    }
    
    public String getDrawingsFolder() {
        return drawingsFolder;
    }
    
    public void setDrawingsFolder(String drawingsFolder) {
        this.drawingsFolder = drawingsFolder;
    }
    
    public String getDxfFolder() {
        return dxfFolder;
    }
    
    public void setDxfFolder(String dxfFolder) {
        this.dxfFolder = dxfFolder;
    }
    
    public TreeMap<String, String> getDrawingPrefixes() {
        return new TreeMap<>(drawingPrefixes);
    }
    
    public void setDrawingPrefixes(TreeMap<String, String> drawingPrefixes) {
        this.drawingPrefixes = new TreeMap<>(drawingPrefixes);
    }
    
    public TreeMap<String, String> getDxfPrefixes() {
        return new TreeMap<>(dxfPrefixes);
    }
    
    public void setDxfPrefixes(TreeMap<String, String> dxfPrefixes) {
        this.dxfPrefixes = new TreeMap<>(dxfPrefixes);
    }
    
    public List<TransmittalPageLayout> getTransmittalLayout() {
        return new ArrayList<>(transmittalLayout);
    }
    
    public void setTransmittalLayout(List<TransmittalPageLayout> transmittalLayout) {
        this.transmittalLayout = new ArrayList<>(transmittalLayout);
    }
}
