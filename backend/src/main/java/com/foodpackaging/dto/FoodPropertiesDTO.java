package com.foodpackaging.dto;
import lombok.Data;
@Data public class FoodPropertiesDTO {
    private Double moistureContent;
    private Double phLevel;
    private Double fatContent;
    private Double proteinContent;
    private Double carbohydrateContent;
    private Double respirationRate;
    private String oxygenSensitivity;
    private String moistureSensitivity;
    private String lightSensitivity;
    private String microbialSensitivity;
    private Boolean isPerishable;
}
