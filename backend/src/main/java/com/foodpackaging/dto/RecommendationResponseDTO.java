package com.foodpackaging.dto;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;
@Data public class RecommendationResponseDTO {
    private Long requestId;
    private String foodCommodityName;
    private String status;
    private List<RecommendationResultDTO> results;
    private LocalDateTime createdAt;
}
