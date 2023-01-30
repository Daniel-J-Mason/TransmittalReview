package Transmittal;

import com.example.transmittalreview.dao.Settings;
import com.example.transmittalreview.entities.BOM;
import com.example.transmittalreview.service.SettingsService;
import com.example.transmittalreview.service.TransmittalService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

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
        bom.getParts().forEach(x -> System.out.println(x.getFullName()));
        bom.getTextFiles().forEach(x -> System.out.println(x.getFullName()));
    }
}
