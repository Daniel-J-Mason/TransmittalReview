package com.example.transmittalreview.entities;

import java.util.Objects;

public class Drawing {
    private String fullName; // can likely be removed as assembly of other part numbers
    private String partNumber;
    private String name;
    private String revisionLevel;
    private String filePath; // May not use
    
    public Drawing(){}
    
    public Drawing(String fullName, String name, String partNumber, String revisionLevel, String filePath){
        this.fullName = fullName;
        this.name = name;
        this.partNumber = partNumber;
        this.revisionLevel = revisionLevel;
        this.filePath = filePath;
    }
    
    public String getPartNumber() {
        return partNumber;
    }
    
    public void setPartNumber(String partNumber) {
        this.partNumber = partNumber;
    }
    
    public String getFullName() {
        return fullName;
    }
    
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getRevisionLevel() {
        return revisionLevel;
    }
    
    public void setRevisionLevel(String revisionLevel) {
        this.revisionLevel = revisionLevel;
    }
    
    public String getFilePath() {
        return filePath;
    }
    
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Drawing drawing)) return false;
        return fullName.equals(drawing.fullName) && partNumber.equals(drawing.partNumber) &&
                Objects.equals(name, drawing.name) && revisionLevel.equals(drawing.revisionLevel) &&
                Objects.equals(filePath, drawing.filePath);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(fullName, partNumber, name, revisionLevel, filePath);
    }
}
