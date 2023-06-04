package com.example.transmittalreview.controllers.entities;

import com.example.transmittalreview.model.entities.Part;
import com.example.transmittalreview.model.entities.Status;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TablePart {
    private Part part;
    private Status status;
}
