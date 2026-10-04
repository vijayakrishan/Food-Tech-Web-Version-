package com.foodpackaging.dto;
import lombok.Data;
import java.util.List;
@Data public class ProducerDashboardDTO {
    private Long totalRecommendations;
    private Long savedRecommendations;
    private Double avgSuitabilityScore;
    private String mostRecommendedMaterial;
    private List<RecommendationHistoryDTO> recentRecommendations;
}
