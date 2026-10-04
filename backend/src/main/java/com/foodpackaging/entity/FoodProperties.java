package com.foodpackaging.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "food_properties")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FoodProperties {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "food_commodity_id")
    private FoodCommodity foodCommodity;

    private Double moistureContent;
    private Double phLevel;
    private Double fatContent;
    private Double proteinContent;
    private Double carbohydrateContent;
    private Double respirationRate;
    private String oxygenSensitivity;
    private String moistureSensitivity;
    private String lightSensitivity;
    private String microbialSensitivity;
    private Boolean isPerishable;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() { createdAt = LocalDateTime.now(); updatedAt = LocalDateTime.now(); }
    @PreUpdate
    protected void onUpdate() { updatedAt = LocalDateTime.now(); }
}
