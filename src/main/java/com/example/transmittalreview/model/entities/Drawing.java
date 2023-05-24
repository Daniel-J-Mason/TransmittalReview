package com.example.transmittalreview.model.entities;

public class Drawing extends Part{
    private String name;
    public Drawing(){}
    
    public Drawing(String fullName, String name, String partNumber, String revisionLevel, String filePath){
        super(fullName, partNumber, revisionLevel, filePath);
        this.name = name;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
}
