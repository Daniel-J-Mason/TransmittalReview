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
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Builder
@Data
public class TransmittalService {
    private TransmittalSettings transmittalSettings;
    
    public List<Part> getParts(File transmittal){
        
        List<Part> partsList = new ArrayList<>();
        
        List<TransmittalSettings.PageLayout> transmittalLayout = transmittalSettings.getPageLayouts();
        
        Workbook workbook = workbookFromFile(transmittal);
        if (workbook == null) return new ArrayList<>();
        
        for (TransmittalSettings.PageLayout pageLayout: transmittalLayout){
            Sheet sheet = workbook.getSheet(pageLayout.getPageName());
            List<Integer> drawingRows = pageLayout.getDrawingRows();
            List<Integer> textRows = pageLayout.getTextRows();
            
            for (Integer rowNumber: drawingRows){
                Row row = sheet.getRow(rowNumber - 1);
                Drawing drawing = drawingFromRow(row);
                if (drawing != null) {
                    partsList.add(drawing);
                }
            }

            for (Integer rowNumber: textRows){
                Row row = sheet.getRow(rowNumber - 1);
                Dxf dxf = dxfFromRow(row);
                if (dxf != null) {
                    partsList.add(dxf);
                }
            }
        }
        return partsList;
    }
    
    private Workbook workbookFromFile(File transmittal){
        Workbook workbook = null;
        try{
            workbook = XSSFWorkbookFactory.createWorkbook(transmittal, true);
            workbook.close();
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
        
        return workbook;
    }
    
    private Drawing drawingFromRow(Row row){
        TransmittalSettings.PageLayout page = layoutFromRow(row);
        
        String name = stringFromCell(row.getCell(page.getNameColumn() - 1));
        String number = stringFromCell(row.getCell(page.getNumberColumn() - 1));
        String revisionLevel = stringFromCell(row.getCell(page.getRevisionColumn() - 1));

        if (!isDrawing(number)) return null;
        
        Pattern revision = Pattern.compile("[A-Za-z]");
        Matcher matcher = revision.matcher(revisionLevel);
        
        return Drawing.builder()
                .prefix(name)
                .partNumber(number)
                .revisionLevel(matcher.matches() ? revisionLevel : null)
                .build();
    }
    
    private Dxf dxfFromRow(Row row){
        TransmittalSettings.PageLayout pageLayout = layoutFromRow(row);
        
        String number = stringFromCell(row.getCell(pageLayout.getNumberColumn() - 1));
        String revisionLevel = stringFromCell(row.getCell(pageLayout.getRevisionColumn() - 1));
        
        if (!isDxf(number)) return null;

        Pattern revision = Pattern.compile("[1-9]+");
        Matcher matcher = revision.matcher(revisionLevel);
        
        return Dxf.builder()
                // remove trailing .TXT/.DXF
                .dxfNumber(number.substring(0, number.indexOf(".")))
                .revisionLevel(matcher.matches() ? revisionLevel : null)
                .build();
    }
    
    private String stringFromCell(Cell cell){
        return switch (cell.getCellType()){
            case NUMERIC -> String.valueOf(cell.getNumericCellValue());
            case STRING -> cell.getStringCellValue();
            case FORMULA -> cell.getRichStringCellValue().toString();
            default -> "";
        };
    }
    
    private boolean isDrawing(String inputText){
        if (inputText == null) return false;
        
        Pattern pattern = Pattern.compile("(\\d{6}[Dd])", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputText);
        return  matcher.matches();
    }
    
    private boolean isDxf(String inputText){
        if (inputText == null) return false;
        
        Pattern pattern = Pattern.compile("(\\d*\\.\\w*)", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputText);
        return  matcher.matches();
    }
    
    private TransmittalSettings.PageLayout layoutFromRow(Row row){
        for (TransmittalSettings.PageLayout page: transmittalSettings.getPageLayouts()){
            if (page.getPageName().equals(row.getSheet().getSheetName())){
                return page;
            }
        }
        return TransmittalSettings.PageLayout.builder().build();
    }
}
