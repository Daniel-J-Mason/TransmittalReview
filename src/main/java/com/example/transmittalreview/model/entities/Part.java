package com.example.transmittalreview.model.entities;

public interface Part {
 
    String getPartNumber();
    
    String getRevisionLevel();
    
    String getFullName();
    
    String getFileName();

    boolean isNew();
    
    void setIsNew(boolean isNew);
}
