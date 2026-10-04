package com.foodpackaging.dto;
import lombok.Data;
import java.time.LocalDateTime;
@Data public class RecommendationHistoryDTO {
    private Long id;
    private String foodCommodityName;
    private String topMaterial;
    private Double topScore;
    private String status;
    private LocalDateTime createdAt;
}
