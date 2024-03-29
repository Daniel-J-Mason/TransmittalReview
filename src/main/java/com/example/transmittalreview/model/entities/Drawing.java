package com.example.transmittalreview.model.entities;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Drawing implements Part{
    
    private String prefix;
    private String partNumber;
    private String revisionLevel;
    private boolean isNew;
    
    public String getFullName(){
        StringBuilder fullName = new StringBuilder();
        if (prefix != null && !prefix.equalsIgnoreCase("")) fullName.append(prefix).append("_");
        fullName.append(getPartNumber());
        if (getRevisionLevel() != null && !getRevisionLevel().equalsIgnoreCase("")) fullName.append("_REV_").append(getRevisionLevel());
        if(isNew) fullName.append("-NEW");
        
        return fullName.toString();
    }
    
    public String getFileName(){
        StringBuilder fileName = new StringBuilder();
        fileName.append(getPartNumber());
        if (getRevisionLevel() != null) {
            fileName.append("_REV_").append(getRevisionLevel()).append(".pdf");
        } else {
            fileName.append(".pdf");
        }
        
        return fileName.toString();
    }
    
    public String toString(){
        return getFullName();
    }
    
    @Override
    public void setIsNew(boolean isNew) {
        this.isNew = isNew;
    }
}
