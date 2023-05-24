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
    exports com.example.transmittalreview.model.service;
    exports com.example.transmittalreview.model.entities;
    exports com.example.transmittalreview.model.dao;
    opens com.example.transmittalreview.model.service to com.google.gson;
    opens com.example.transmittalreview.model.dao to com.google.gson;
    opens com.example.transmittalreview to javafx.fxml;
    opens com.example.transmittalreview.controllers to javafx.fxml;
    opens com.example.transmittalreview.model.entities to com.google.gson;
}
