package com.example.transmittalreview;

import com.example.transmittalreview.controllers.MainViewController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Objects;

public class TransmittalReviewApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(TransmittalReviewApplication.class.getResource("main.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1000, 800);
        
        MainViewController controller = fxmlLoader.getController();
        controller.setHostServices(getHostServices());
        
        stage.setTitle("Transmittal Review");
        stage.setScene(scene);
        stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("images/Transmittal-logo.ico"))));
        stage.show();
    }
    
    public static void main(String[] args) {
        launch();
    }
}