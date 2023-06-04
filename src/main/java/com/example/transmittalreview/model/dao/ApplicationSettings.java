package com.example.transmittalreview.model.dao;

import lombok.Builder;
import lombok.Data;

import java.util.TreeMap;

@Data
@Builder
public class ApplicationSettings {
    private String engineeringWorkingFolder;
    private String drawingsFolder;
    private String dxfFolder;
    private TreeMap<Integer, Integer> drawingPrefixes;
    private TreeMap<Integer, Integer> dxfPrefixes;
    private String defaultTransmittalLayout;
    private boolean isDarkMode;
}
