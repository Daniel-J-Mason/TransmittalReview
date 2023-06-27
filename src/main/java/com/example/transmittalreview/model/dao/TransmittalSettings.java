package com.example.transmittalreview.model.dao;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class TransmittalSettings {
    List<PageLayout> pageLayouts;
    
    @Data
    @Builder
    public static class PageLayout {
        private String pageName;
        private List<Integer> drawingRows;
        private List<Integer> skinRows;
        private List<Integer> textRows;
        private Integer nameColumn;
        private Integer numberColumn;
        private Integer revisionColumn;
    }
}
