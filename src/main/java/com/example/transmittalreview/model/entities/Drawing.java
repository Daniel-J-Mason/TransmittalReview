package com.example.transmittalreview.model.entities;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Drawing implements Part{
    
    private String prefix;
    private String partNumber;
    private String revisionLevel;
    
    public String getFullName(){
        StringBuilder fullName = new StringBuilder();
        if (prefix != null) fullName.append(prefix).append("_");
        fullName.append(getPartNumber());
        if (getRevisionLevel() != null) fullName.append("_").append(getRevisionLevel());
        
        return fullName.toString();
    }
    
    public String getFileName(){
        StringBuilder fileName = new StringBuilder();
        fileName.append(getPartNumber());
        if (getRevisionLevel() != null) fileName.append("_").append(getRevisionLevel()).append(".pdf");
        
        return fileName.toString();
    }
    
    public String toString(){
        return getFullName();
    }
}
