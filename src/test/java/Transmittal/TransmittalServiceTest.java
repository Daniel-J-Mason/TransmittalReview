package Transmittal;

import com.example.transmittalreview.model.dao.Settings;
import com.example.transmittalreview.model.entities.BOM;
import com.example.transmittalreview.model.service.BOMService;
import com.example.transmittalreview.model.service.ComparisonReport;
import com.example.transmittalreview.model.service.SettingsService;
import com.example.transmittalreview.model.service.TransmittalService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

public class TransmittalServiceTest {
    Settings settings;
    @BeforeEach
    void loadService(){
        File file = new File(getClass().getResource("/transmittalreview/Transmittal/Wheeled_Coach.json").getFile());
        SettingsService service = new SettingsService();
        service.loadSettings(file);
        settings = service.getSettings();
    }
    
    @Test
    void transmittal(){
        File file = new File(getClass().getResource("/transmittalreview/Transmittal/0000_Test_Excel.xlsm").getFile());
        TransmittalService transmittalService = new TransmittalService(file, settings);
        BOM bom = transmittalService.getBom();
        File bomFile = new File(getClass().getResource("/transmittalreview/BOM/test.bom.1").getFile());
        BOMService bomService = new BOMService(bomFile);
        BOM bom2 = bomService.getBom();
        ComparisonReport comparisonReport = new ComparisonReport(bom, bom2);
        comparisonReport.report().forEach(element -> System.out.println(element.getFullName() + " %"+ element.getRevisionLevel() + ": " + element.getStatus()));
    }
}
