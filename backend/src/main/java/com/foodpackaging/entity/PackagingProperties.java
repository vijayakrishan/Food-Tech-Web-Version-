package com.foodpackaging.entity;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity @Table(name = "packaging_properties")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class PackagingProperties {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @OneToOne(fetch = FetchType.LAZY) @JoinColumn(name = "packaging_material_id") private PackagingMaterial packagingMaterial;
    private Double oxygenTransmissionRate;
    private Double waterVaporTransmissionRate;
    private Double lightTransmission;
    private Double thicknessMicrons;
    private Double tensileStrength;
    private String flexibility;
    private Boolean heatSealability;
    private Double transparency;
    private Double recyclabilityScore;
    private Double biodegradabilityScore;
    private Double estimatedCostPerSqm;
    private Double sustainabilityScore;
    private Double maxTemperature;
    private Double minTemperature;
    private Double greaseResistance;
    @Column(name = "created_at") private LocalDateTime createdAt;
    @Column(name = "updated_at") private LocalDateTime updatedAt;
    @PrePersist protected void onCreate() { createdAt = LocalDateTime.now(); updatedAt = LocalDateTime.now(); }
    @PreUpdate protected void onUpdate() { updatedAt = LocalDateTime.now(); }
}
