package com.foodpackaging.dto;
import lombok.Data;
@Data public class RecommendationFactorDTO {
    private String factorName;
    private Double factorValue;
    private Double contributionPercentage;
    private String direction;
}
