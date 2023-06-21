package com.example.transmittalreview.model.entities;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Dxf implements Part {
    
    private String dxfNumber;
    private String revisionLevel;
    private boolean isNew;
    
    public String getPartNumber(){
        return dxfNumber.substring(0, 6) + "D";
    }
    
    public String getFullName(){
        StringBuilder fullName = new StringBuilder();
        fullName.append(getDxfNumber());
        if (getRevisionLevel() != null  && !getRevisionLevel().equalsIgnoreCase("")) fullName.append("_REV_").append(getRevisionLevel());
        return fullName.toString();
    }
    
    public String getFileName(){
        StringBuilder fileName = new StringBuilder();
        fileName.append(getDxfNumber());
        if (getRevisionLevel() != null) fileName.append("_").append(getRevisionLevel()).append(".dxf");
        
        return fileName.toString();
    }
    
    public String toString(){
        return getFullName();
    }
}
