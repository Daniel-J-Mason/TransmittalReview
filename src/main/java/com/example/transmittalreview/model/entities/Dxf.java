package com.example.transmittalreview.model.entities;

public class Dxf extends Part {
    private String dxfNumber;
    
    public Dxf(String fullName, String partNumber, String dxfNumber, String revisionLevel, String filePath) {
        super(fullName, partNumber, revisionLevel, filePath);
        this.dxfNumber = dxfNumber;
    }
    
    public Dxf() {
        super();
    }
    public String getDxfNumber() {
        return dxfNumber;
    }
    
    public void setDxfNumber(String dxfNumber) {
        this.dxfNumber = dxfNumber;
    }
}
