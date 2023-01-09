module com.example.transmittalreview {
    requires javafx.controls;
    requires javafx.fxml;
    
    requires org.controlsfx.controls;
    
    opens com.example.transmittalreview to javafx.fxml;
    exports com.example.transmittalreview;
}