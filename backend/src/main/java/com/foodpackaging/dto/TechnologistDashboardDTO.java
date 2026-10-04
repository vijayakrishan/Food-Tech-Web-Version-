package com.foodpackaging.dto;
import lombok.Data;
@Data public class TechnologistDashboardDTO {
    private Long totalFoodCommodities;
    private Long totalPackagingMaterials;
    private Double modelAccuracy;
    private String modelVersion;
    private Long totalRecommendations;
}
