package com.example.transmittalreview.model.service;

import com.example.transmittalreview.model.dao.Settings;
import com.example.transmittalreview.model.entities.BOM;
import com.example.transmittalreview.model.entities.Drawing;
import com.example.transmittalreview.model.entities.Dxf;
import com.example.transmittalreview.model.dao.TransmittalPageLayout;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbookFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//TODO file validation
public class TransmittalService {
    private Settings settings;
    private File file;
    
    public TransmittalService(File file, Settings settings){
        this.settings = settings;
        this.file = file;
    }
    
    public File getFile() {
        return file;
    }
    
    public void setFile(File file) {
        this.file = file;
    }
    
    public Settings getSettings() {
        return settings;
    }
    
    public void setSettings(Settings settings) {
        this.settings = settings;
    }
    
    public BOM getBom(){
        List<TransmittalPageLayout> transmittalLayout = settings.getTransmittalLayout();
        List<Drawing> drawings = new ArrayList<>();
        List<Dxf> textFiles = new ArrayList<>();
        
        Workbook workbook = workbookFromFile(file);
        if (workbook == null){
            return new BOM();
        }
        
        for (TransmittalPageLayout page: transmittalLayout){
            Sheet sheet = workbook.getSheet(page.getPageName());
            List<Integer> drawingRows = page.getDrawingRows();
            List<Integer> textRows = page.getTextRows();
            
            for (Integer rowNumber: drawingRows){
                Row row = sheet.getRow(rowNumber - 1);
                Drawing drawing = drawingFromRow(row);
                if (drawing != null) {
                    drawings.add(drawing);
                }
            }
            
            for (Integer rowNumber: textRows){
                Row row = sheet.getRow(rowNumber - 1);
                textFiles.add(dxfFromRow(row));
            }
        }
        return new BOM(drawings, textFiles);
    }
    
    private Workbook workbookFromFile(File file){
        Workbook workbook = null;
        try{
            workbook = XSSFWorkbookFactory.createWorkbook(file, true);
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
        
        return workbook;
    }
    
    private Drawing drawingFromRow(Row row){
        TransmittalPageLayout page = layoutFromRow(row);
        Cell nameCell = row.getCell(page.getNameColumn());
        Cell numberCell = row.getCell(page.getNumberColumn());
        Cell revisionCell = row.getCell(page.getRevisionColumn());
        String name = stringFromCell(nameCell);
        String number = stringFromCell(numberCell);
        if (!isDrawing(number)){
            return null;
        }
        String revision;
        Pattern pattern = Pattern.compile("[A-Za-z]");
        Matcher matcher = pattern.matcher("");
        revision = stringFromCell(revisionCell);
        if (revision.contains("NEW")){
            revision = "0";
        }
        matcher.reset(revision);
        StringBuilder fullname = new StringBuilder();
        if (name != null && !name.isEmpty()){
            fullname.append(name).append("_");
        }
        fullname.append(number);
        if (matcher.matches()){
            fullname.append("_REV_").append(revision);
        }
        
        return new Drawing(fullname.toString(), name, number, revision, "");
    }
    
    private Dxf dxfFromRow(Row row){
        TransmittalPageLayout page = layoutFromRow(row);
        
        String dxfCellValue = stringFromCell(row.getCell(page.getNumberColumn()));
        String dxfNumber = dxfNumberFromCell(dxfCellValue);
        String partNumber = dxfNumber.substring(0,6) + "D"; //TODO method to parse part no.
        String revisionLevel = stringFromCell(row.getCell(page.getRevisionColumn()));
        StringBuilder fullName = new StringBuilder(partNumber);
        Pattern pattern = Pattern.compile("[1-9]+");
        if (pattern.matcher(revisionLevel).matches()){
            fullName.append("_REV_").append(revisionLevel);
        }
        fullName.append(".dxf");
        return new Dxf(fullName.toString(), partNumber, dxfNumber, revisionLevel, "");
    }
    
    private String dxfNumberFromCell(String cellValue){
        if (cellValue.length() == 0){
            return cellValue;
        }
        return cellValue.substring(0, cellValue.indexOf("."));
    }
    
    private boolean rowContainsDrawing(Row row){
        TransmittalPageLayout pageLayout = layoutFromRow(row);
        String drawingNumber = stringFromCell(row.getCell(pageLayout.getNumberColumn()));
        return isDrawing(drawingNumber);
    }
    
    private String stringFromCell(Cell cell){
        return switch (cell.getCellType()){
            case NUMERIC -> String.valueOf(cell.getNumericCellValue());
            case STRING -> cell.getStringCellValue();
            default -> "";
        };
    }
    
    private boolean isDrawing(String inputText){
        Pattern pattern = Pattern.compile("(\\w*)?(\\d{6}[Dd])(_rev_(\\w))?(\\w*)?", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputText);
        return  matcher.matches();
    }
    
    private TransmittalPageLayout layoutFromRow(Row row){
        for (TransmittalPageLayout page: settings.getTransmittalLayout()){
            if (page.getPageName().equals(row.getSheet().getSheetName())){
                return page;
            }
        }
        return new TransmittalPageLayout();
    }
}
