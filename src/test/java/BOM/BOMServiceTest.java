package BOM;

import com.example.transmittalreview.model.entities.Drawing;
import com.example.transmittalreview.model.service.BOMService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class BOMServiceTest {
    
    @ParameterizedTest
    @CsvFileSource(resources = "/transmittalreview/BOM/String.csv")
    public void stringParse(String input, String expectedList) {
        BOMService testService = new BOMService(input);
        List<String> processedList = testService.getBom().getParts().stream().map(Drawing::getPartNumber).toList();
        List<String> expected = new ArrayList<>(Arrays.asList(expectedList.split(",")));
        Assertions.assertEquals(processedList.size(), expected.size());
        Assertions.assertTrue(processedList.containsAll(expected));
        Assertions.assertTrue(expected.containsAll(processedList));
    }
    
    @ParameterizedTest
    @CsvFileSource(resources = "/transmittalreview/BOM/File.csv")
    public void fileParse(String resourceFilepath, String expectedList){
        File inputFile = new File(Objects.requireNonNull(getClass().getResource(resourceFilepath)).getFile());
        BOMService testService = new BOMService(inputFile);
        List<String> processedList = testService.getBom().getParts().stream().map(Drawing::getFullName).toList();
        List<String> expected = new ArrayList<>(Arrays.asList(expectedList.split(",")));
        Assertions.assertEquals(processedList.size(), expected.size());
        Assertions.assertTrue(processedList.containsAll(expected));
        Assertions.assertTrue(expected.containsAll(processedList));
    }
    
    @ParameterizedTest
    @CsvFileSource(resources = "/transmittalreview/BOM/Clipboard.csv")
    public void clipboardParse(String clipboardFile, String expectedList) {
        File inputFile = new File(Objects.requireNonNull(getClass().getResource(clipboardFile)).getFile());
        BOMService testService = new BOMService(inputFile);
        List<String> processedList = testService.getBom().getParts().stream().map(Drawing::getFullName).toList();
        List<String> expected = new ArrayList<>(Arrays.asList(expectedList.split(",")));
        Assertions.assertEquals(processedList.size(), expected.size());
        Assertions.assertTrue(processedList.containsAll(expected));
        Assertions.assertTrue(expected.containsAll(processedList));
    }
    
    @Test
    public void nullFile(){
        File file = null;
        BOMService testService = new BOMService(file);
        Assertions.assertEquals(testService.getBom().getParts(), new ArrayList<>());
    }
}
