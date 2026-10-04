package com.foodpackaging.entity;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity @Table(name = "recommendation_requests")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class RecommendationRequest {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "user_id") private User user;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "food_commodity_id") private FoodCommodity foodCommodity;
    
    private Double moistureContent;
    private Double phLevel;
    private Double fatContent;
    private Double proteinContent;
    private Double carbohydrateContent;
    private Double respirationRate;
    private String oxygenSensitivity;
    private String moistureSensitivity;
    private String lightSensitivity;
    
    private Double storageTemperature;
    private Double storageHumidity;
    private Integer requiredShelfLifeDays;
    private Boolean isRefrigerated;
    private String lightExposure;
    
    private Double costPreference;
    private Double sustainabilityPreference;
    private Double performancePreference;
    private Double shelfLifePreference;
    
    private String status;
    private String modelVersion;
    
    @Column(name = "created_at") private LocalDateTime createdAt;
    
    @OneToMany(mappedBy = "recommendationRequest", cascade = CascadeType.ALL)
    private List<RecommendationResult> results;
    
    @PrePersist protected void onCreate() { createdAt = LocalDateTime.now(); }
}
