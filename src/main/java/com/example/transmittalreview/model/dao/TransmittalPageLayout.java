package com.example.transmittalreview.model.dao;

import java.util.ArrayList;
import java.util.List;

public class TransmittalPageLayout {
    private String pageName;
    private List<Integer> drawingRows;
    private List<Integer> textRows;
    private Integer nameColumn;
    private Integer numberColumn;
    private Integer revisionColumn;
    
    public TransmittalPageLayout(){
        drawingRows = new ArrayList<>();
        textRows = new ArrayList<>();
    }
    
    public TransmittalPageLayout(String pageName, List<Integer> drawingRows, List<Integer> textRows){
        this.pageName = pageName;
        this.drawingRows = new ArrayList<>(drawingRows);
        this.textRows = new ArrayList<>(textRows);
    }
    
    public List<Integer> getDrawingRows() {
        return drawingRows;
    }
    
    public void setDrawingRows(List<Integer> drawingRows) {
        this.drawingRows = drawingRows;
    }
    
    public List<Integer> getTextRows() {
        return new ArrayList<>(textRows);
    }
    
    public void setTextRows(List<Integer> textRows) {
        this.textRows = new ArrayList<>(textRows);
    }
    
    public String getPageName() {
        return pageName;
    }
    
    public void setPageName(String pageName) {
        this.pageName = pageName;
    }
    
    public Integer getNameColumn() {
        return nameColumn;
    }
    
    public void setNameColumn(Integer nameColumn) {
        this.nameColumn = nameColumn;
    }
    
    public Integer getNumberColumn() {
        return numberColumn;
    }
    
    public void setNumberColumn(Integer numberColumn) {
        this.numberColumn = numberColumn;
    }
    
    public Integer getRevisionColumn() {
        return revisionColumn;
    }
    
    public void setRevisionColumn(Integer revisionColumn) {
        this.revisionColumn = revisionColumn;
    }
}
