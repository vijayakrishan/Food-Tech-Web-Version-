package com.foodpackaging.dto;
import lombok.Data;
import java.util.List;
@Data public class RecommendationResultDTO {
    private Long materialId;
    private String materialName;
    private String materialCode;
    private Integer rank;
    private Double suitabilityScore;
    private Double performanceScore;
    private Double costScore;
    private Double sustainabilityScore;
    private Double overallScore;
    private String explanation;
    private List<RecommendationFactorDTO> factors;
    private PackagingPropertiesDTO materialProperties;
}
