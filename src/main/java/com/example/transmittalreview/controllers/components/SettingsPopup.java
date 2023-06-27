package com.example.transmittalreview.controllers.components;

import javafx.scene.control.Label;
import javafx.stage.Popup;
import javafx.stage.PopupWindow;

public class SettingsPopup extends Popup {
    public SettingsPopup(){
        super();
        Label test = new Label("Test");
        this.getContent().add(test);
    }
}
