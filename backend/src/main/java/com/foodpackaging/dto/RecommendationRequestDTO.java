package com.foodpackaging.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecommendationRequestDTO {
    private Long foodCommodityId;

    // Food properties
    private Double moistureContent;
    private Double phLevel;
    private Double fatContent;
    private Double proteinContent;
    private Double carbohydrateContent;
    private Double respirationRate;
    private String oxygenSensitivity;
    private String moistureSensitivity;
    private String lightSensitivity;

    // Storage conditions
    private Double storageTemperature;
    private Double storageHumidity;
    private Integer requiredShelfLifeDays;
    private Boolean isRefrigerated;
    private String lightExposure;

    // Multi-objective preferences (0.0 to 1.0)
    private Double costPreference;
    private Double sustainabilityPreference;
    private Double performancePreference;
    private Double shelfLifePreference;
}
