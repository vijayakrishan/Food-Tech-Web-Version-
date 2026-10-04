package com.foodpackaging.entity;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity @Table(name = "model_versions")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class ModelVersion {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    private String modelName;
    private String algorithm;
    private String version;
    private LocalDateTime trainingDate;
    private Integer datasetSize;
    private Double accuracy;
    private Double precisionScore;
    private Double recallScore;
    private Double f1Score;
    private Double rmse;
    private Double r2Score;
    private Double mae;
    private Boolean isActive;
    private String modelPath;
    @Column(name = "created_at") private LocalDateTime createdAt;
    @PrePersist protected void onCreate() { createdAt = LocalDateTime.now(); }
}
