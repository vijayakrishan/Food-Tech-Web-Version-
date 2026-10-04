package com.foodpackaging.entity;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity @Table(name = "recommendation_factors")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class RecommendationFactor {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "result_id") private RecommendationResult recommendationResult;
    
    private String factorName;
    private Double factorValue;
    private Double contributionPercentage;
    private String direction;
    
    @Column(name = "created_at") private LocalDateTime createdAt;
    @PrePersist protected void onCreate() { createdAt = LocalDateTime.now(); }
}
