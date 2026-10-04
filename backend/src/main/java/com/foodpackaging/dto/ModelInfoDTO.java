package com.foodpackaging.dto;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.Map;
@Data public class ModelInfoDTO {
    private String modelName;
    private String algorithm;
    private String version;
    private LocalDateTime trainingDate;
    private Integer datasetSize;
    private Map<String, Double> metrics;
}
