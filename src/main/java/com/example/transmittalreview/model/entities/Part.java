package com.example.transmittalreview.model.entities;

import java.util.Objects;

public abstract class Part {
    private String fullName;
    private String partNumber;
    private String revisionLevel;
    private String filePath;
    private Status STATUS;
    
    public Part(){};
    
    public Part(String fullName, String partNumber, String revisionLevel, String filePath){
        this.fullName = fullName;
        this.partNumber = partNumber;
        this.revisionLevel = revisionLevel;
        this.filePath = filePath;
        this.STATUS = Status.DEFAULT;
    }
    
    public String getFullName() {
        return fullName;
    }
    
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    
    public String getPartNumber() {
        return partNumber;
    }
    
    public void setPartNumber(String partNumber) {
        this.partNumber = partNumber;
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
    
    public Status getStatus() {
        return STATUS;
    }
    
    public void setStatus(Status STATUS) {
        this.STATUS = STATUS;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Part part)) return false;
        return Objects.equals(fullName, part.fullName) && Objects.equals(partNumber, part.partNumber)
                && Objects.equals(revisionLevel, part.revisionLevel) && Objects.equals(filePath, part.filePath);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(fullName, partNumber, revisionLevel, filePath);
    }
}
