import os

base_dir = r"c:\Users\Vijayakrishnan.R\OneDrive\Desktop\Food tech\ai-food-packaging\backend"
src_main_java = os.path.join(base_dir, "src", "main", "java", "com", "foodpackaging")

def write_file(path, content):
    os.makedirs(os.path.dirname(path), exist_ok=True)
    with open(path, 'w', encoding='utf-8') as f:
        f.write(content.strip() + '\n')

files = {
    os.path.join(src_main_java, "entity", "PackagingMaterial.java"): """package com.foodpackaging.entity;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity @Table(name = "packaging_materials")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class PackagingMaterial {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(nullable = false) private String name;
    @Column(nullable = false, unique = true) private String materialCode;
    private String category;
    private String description;
    @Column(name = "created_at") private LocalDateTime createdAt;
    @Column(name = "updated_at") private LocalDateTime updatedAt;
    @OneToOne(mappedBy = "packagingMaterial", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private PackagingProperties packagingProperties;
    @PrePersist protected void onCreate() { createdAt = LocalDateTime.now(); updatedAt = LocalDateTime.now(); }
    @PreUpdate protected void onUpdate() { updatedAt = LocalDateTime.now(); }
}""",
    
    os.path.join(src_main_java, "entity", "PackagingProperties.java"): """package com.foodpackaging.entity;
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
}""",

    os.path.join(src_main_java, "entity", "RecommendationRequest.java"): """package com.foodpackaging.entity;
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
    private Boolean lightExposure;
    
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
}""",

    os.path.join(src_main_java, "entity", "RecommendationResult.java"): """package com.foodpackaging.entity;
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
}""",

    os.path.join(src_main_java, "entity", "RecommendationFactor.java"): """package com.foodpackaging.entity;
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
}""",

    os.path.join(src_main_java, "entity", "SavedRecommendation.java"): """package com.foodpackaging.entity;
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
}""",

    os.path.join(src_main_java, "entity", "ModelVersion.java"): """package com.foodpackaging.entity;
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
}""",

    os.path.join(src_main_java, "entity", "AuditLog.java"): """package com.foodpackaging.entity;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity @Table(name = "audit_logs")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class AuditLog {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "user_id", nullable = true) private User user;
    private String action;
    private String entityType;
    private Long entityId;
    @Column(columnDefinition = "TEXT") private String details;
    private String ipAddress;
    @Column(name = "created_at") private LocalDateTime createdAt;
    @PrePersist protected void onCreate() { createdAt = LocalDateTime.now(); }
}"""
}

for path, content in files.items():
    write_file(path, content)
print("Entities part 4 generated.")
