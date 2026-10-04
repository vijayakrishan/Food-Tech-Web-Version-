package com.foodpackaging.entity;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity @Table(name = "recommendation_results")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class RecommendationResult {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "request_id") private RecommendationRequest recommendationRequest;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "packaging_material_id") private PackagingMaterial packagingMaterial;
    
    private Integer rank;
    private Double suitabilityScore;
    private Double performanceScore;
    private Double costScore;
    private Double sustainabilityScore;
    private Double overallScore;
    
    @Column(columnDefinition = "TEXT") private String explanation;
    @Column(name = "created_at") private LocalDateTime createdAt;
    
    @OneToMany(mappedBy = "recommendationResult", cascade = CascadeType.ALL)
    private List<RecommendationFactor> factors;
    
    @PrePersist protected void onCreate() { createdAt = LocalDateTime.now(); }
}
