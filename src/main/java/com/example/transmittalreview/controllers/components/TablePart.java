package com.example.transmittalreview.controllers.components;

import com.example.transmittalreview.model.entities.Part;
import com.example.transmittalreview.model.entities.Status;
import javafx.scene.control.Hyperlink;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Builder
public class TablePart {
    private Part part;
    private Status status;
}
