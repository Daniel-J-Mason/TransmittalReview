module com.example.transmittalreview {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    
    requires org.controlsfx.controls;
    requires com.google.gson;
    requires poi;
    requires poi.ooxml;
    
    exports com.example.transmittalreview;
    exports com.example.transmittalreview.controllers;
    exports com.example.transmittalreview.service;
    exports com.example.transmittalreview.entities;
    exports com.example.transmittalreview.dao;
    opens com.example.transmittalreview.service to com.google.gson;
    opens com.example.transmittalreview.dao to com.google.gson;
    opens com.example.transmittalreview to javafx.fxml;
    opens com.example.transmittalreview.controllers to javafx.fxml;
    opens com.example.transmittalreview.entities to com.google.gson;
}
