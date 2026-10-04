package com.foodpackaging.entity;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity @Table(name = "saved_recommendations", uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "request_id"})})
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class SavedRecommendation {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "user_id") private User user;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "request_id") private RecommendationRequest recommendationRequest;
    
    @Column(columnDefinition = "TEXT") private String notes;
    @Column(name = "created_at") private LocalDateTime createdAt;
    @PrePersist protected void onCreate() { createdAt = LocalDateTime.now(); }
}
