package com.example.transmittalreview.model.service;

import com.example.transmittalreview.model.dao.TransmittalSettings;
import com.example.transmittalreview.model.entities.Drawing;
import com.example.transmittalreview.model.entities.Dxf;
import com.example.transmittalreview.model.entities.Part;
import lombok.Builder;
import lombok.Data;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbookFactory;

import java.io.File;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Builder
@Data
public class TransmittalService {
    private TransmittalSettings transmittalSettings;
    
    public List<Part> getParts(File transmittal) {
        
        List<Part> partsList = new ArrayList<>();
        
        List<TransmittalSettings.PageLayout> transmittalLayout = transmittalSettings.getPageLayouts();
        
        Workbook workbook = workbookFromFile(transmittal);
        if (workbook == null) return new ArrayList<>();
        
        for (TransmittalSettings.PageLayout pageLayout : transmittalLayout) {
            Sheet sheet = workbook.getSheet(pageLayout.getPageName());
            List<Integer> drawingRows = pageLayout.getDrawingRows();
            List<Integer> skinRows = pageLayout.getSkinRows();
            List<Integer> textRows = pageLayout.getTextRows();
            
            //Maintain Transmittal order for aesthetic display
            Map<Integer, String> allRows = combineRows(drawingRows, skinRows, textRows);
            
            partsList.addAll(generateSheetPartsList(allRows, sheet));
            
        }
        return partsList;
    }
    
    private Workbook workbookFromFile(File transmittal) {
        Workbook workbook = null;
        try {
            workbook = XSSFWorkbookFactory.createWorkbook(transmittal, true);
            workbook.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        
        return workbook;
    }
    
    private Drawing drawingFromRow(Row row) {
        TransmittalSettings.PageLayout page = layoutFromRow(row);
        
        String name = stringFromCell(row.getCell(page.getNameColumn() - 1));
        String number = stringFromCell(row.getCell(page.getNumberColumn() - 1));
        String revisionLevel = stringFromCell(row.getCell(page.getRevisionColumn() - 1));
        
        if (!isDrawing(number)) return null;
        
        Pattern revision = Pattern.compile("[A-Za-z]");
        Matcher matcher = revision.matcher(revisionLevel);
        
        boolean isNew = false;
        
        for (int i = 1; i < 8; i++) {
            if (stringFromCell(row.getCell(i)).contains("NEW")) {
                isNew = true;
            }
        }
        
        return Drawing.builder()
                .prefix(name)
                .partNumber(number)
                .revisionLevel(matcher.matches() ? revisionLevel : null)
                .isNew(isNew)
                .build();
    }
    
    private Dxf dxfFromRow(Row row) {
        TransmittalSettings.PageLayout pageLayout = layoutFromRow(row);
        
        String number = stringFromCell(row.getCell(pageLayout.getNumberColumn() - 1));
        String revisionLevel = stringFromCell(row.getCell(pageLayout.getRevisionColumn() - 1));
        
        if (!isDxf(number)) return null;
        
        Pattern revision = Pattern.compile("[1-9]+");
        Matcher matcher = revision.matcher(revisionLevel);
        
        boolean isNew = false;
        
        for (int i = 1; i < 8; i++) {
            if (stringFromCell(row.getCell(i)).contains("NEW")) {
                isNew = true;
            }
        }
        
        return Dxf.builder()
                // remove trailing .TXT/.DXF
                .dxfNumber(number.substring(0, number.indexOf(".")))
                .revisionLevel(matcher.matches() ? revisionLevel : null)
                .isNew(isNew)
                .build();
    }
    
    private String stringFromCell(Cell cell) {
        return switch (cell.getCellType()) {
            case NUMERIC -> String.valueOf(cell.getNumericCellValue());
            case STRING -> cell.getStringCellValue();
            case FORMULA -> cell.getRichStringCellValue().toString();
            default -> "";
        };
    }
    
    private List<Part> generateSheetPartsList(Map<Integer, String> combinedRowList, Sheet sheet){
        List<Part> result = new ArrayList<>();
        
        for (Integer rowNumber : combinedRowList.keySet()) {
            switch (combinedRowList.get(rowNumber)) {
                case "drawing" -> {
                    Row row = sheet.getRow(rowNumber - 1);
                    Drawing drawing = drawingFromRow(row);
                    if (drawing != null) {
                        result.add(drawing);
                    }
                }
                case "text" -> {
                    Row row = sheet.getRow(rowNumber - 1);
                    Dxf dxf = dxfFromRow(row);
                    if (dxf != null) {
                        result.add(dxf);
                    }
                }
                
                case "skin" -> {
                    Row row = sheet.getRow(rowNumber - 1);
                    Dxf dxf = dxfFromRow(row);
                    if (dxf != null) {
                        dxf.setSkin(true);
                        result.add(dxf);
                    }
                }
            }
        }
        
        return result;
        
    }
    
    private Map<Integer, String> combineRows(List<Integer> drawings, List<Integer> skins, List<Integer> texts){
        
        Map<Integer, String> result = new TreeMap<>();
        
        for (Integer drawing : drawings) {
            result.put(drawing, "drawing");
        }
        
        for (Integer skin : skins) {
            result.put(skin, "skin");
        }
        
        for (Integer text : texts) {
            result.put(text, "text");
        }
        
        return result;
    }
    
    private boolean isDrawing(String inputText) {
        if (inputText == null) return false;
        
        Pattern pattern = Pattern.compile("(\\d{6}[Dd])", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputText);
        return matcher.matches();
    }
    
    private boolean isDxf(String inputText) {
        if (inputText == null) return false;
        
        Pattern pattern = Pattern.compile("(\\d*\\.\\w*)", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputText);
        return matcher.matches();
    }
    
    private TransmittalSettings.PageLayout layoutFromRow(Row row) {
        for (TransmittalSettings.PageLayout page : transmittalSettings.getPageLayouts()) {
            if (page.getPageName().equals(row.getSheet().getSheetName())) {
                return page;
            }
        }
        return TransmittalSettings.PageLayout.builder().build();
    }
}
