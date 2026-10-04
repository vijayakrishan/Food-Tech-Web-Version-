package com.foodpackaging.dto;
import lombok.Data;
import java.util.List;
@Data public class AdminDashboardDTO {
    private Long totalUsers;
    private Long activeUsers;
    private Long producerCount;
    private Long technologistCount;
    private Long totalRecommendations;
    private List<String> mostRecommendedMaterials;
    private List<String> recentActivity;
}
