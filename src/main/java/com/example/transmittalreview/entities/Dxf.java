package com.example.transmittalreview.entities;

public class Dxf {
    private String fullName;
    private String partNumber;
    private String dxfNumber;
    private String revisionLevel;
    
    public Dxf(String fullName, String partNumber, String dxfNumber, String revisionLevel) {
        this.fullName = fullName;
        this.partNumber = partNumber;
        this.dxfNumber = dxfNumber;
        this.revisionLevel = revisionLevel;
    }
    
    public Dxf() {}
    
    public String getFullName() {
        return fullName;
    }
    
    public void setFullname(String fullName) {
        this.fullName = fullName;
    }
    
    public String getPartNumber() {
        return partNumber;
    }
    
    public void setPartNumber(String partNumber) {
        this.partNumber = partNumber;
    }
    
    public String getDxfNumber() {
        return dxfNumber;
    }
    
    public void setDxfNumber(String dxfNumber) {
        this.dxfNumber = dxfNumber;
    }
    
    public String getRevisionLevel() {
        return revisionLevel;
    }
    
    public void setRevisionLevel(String revisionLevel) {
        this.revisionLevel = revisionLevel;
    }
}
