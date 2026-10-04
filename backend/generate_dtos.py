import os

base_dir = r"c:\Users\Vijayakrishnan.R\OneDrive\Desktop\Food tech\ai-food-packaging\backend"
src_main_java = os.path.join(base_dir, "src", "main", "java", "com", "foodpackaging")

def write_file(path, content):
    os.makedirs(os.path.dirname(path), exist_ok=True)
    with open(path, 'w', encoding='utf-8') as f:
        f.write(content.strip() + '\n')

files = {
    os.path.join(src_main_java, "dto", "LoginRequest.java"): """package com.foodpackaging.dto;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
@Data public class LoginRequest {
    @NotBlank private String username;
    @NotBlank private String password;
}""",

    os.path.join(src_main_java, "dto", "RegisterRequest.java"): """package com.foodpackaging.dto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
@Data public class RegisterRequest {
    @NotBlank private String username;
    @NotBlank @Email private String email;
    @NotBlank private String password;
    private String firstName;
    private String lastName;
    private String phone;
    private String organization;
    private String role;
}""",

    os.path.join(src_main_java, "dto", "AuthResponse.java"): """package com.foodpackaging.dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
@Data @Builder @AllArgsConstructor public class AuthResponse {
    private String token;
    @Builder.Default private String type = "Bearer";
    private Long id;
    private String username;
    private String email;
    private String role;
}""",

    os.path.join(src_main_java, "dto", "FoodCommodityDTO.java"): """package com.foodpackaging.dto;
import lombok.Data;
@Data public class FoodCommodityDTO {
    private Long id;
    private String name;
    private Long categoryId;
    private String categoryName;
    private String description;
    private String imageUrl;
    private FoodPropertiesDTO properties;
}""",

    os.path.join(src_main_java, "dto", "FoodPropertiesDTO.java"): """package com.foodpackaging.dto;
import lombok.Data;
@Data public class FoodPropertiesDTO {
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
}""",

    os.path.join(src_main_java, "dto", "FoodCategoryDTO.java"): """package com.foodpackaging.dto;
import lombok.Data;
@Data public class FoodCategoryDTO {
    private Long id;
    private String name;
    private String description;
    private Integer commodityCount;
}""",

    os.path.join(src_main_java, "dto", "CreateFoodCommodityRequest.java"): """package com.foodpackaging.dto;
import lombok.Data;
@Data public class CreateFoodCommodityRequest {
    private String name;
    private Long categoryId;
    private String description;
    private FoodPropertiesDTO properties;
}""",

    os.path.join(src_main_java, "dto", "PackagingMaterialDTO.java"): """package com.foodpackaging.dto;
import lombok.Data;
@Data public class PackagingMaterialDTO {
    private Long id;
    private String name;
    private String materialCode;
    private String category;
    private String description;
    private PackagingPropertiesDTO properties;
}""",

    os.path.join(src_main_java, "dto", "PackagingPropertiesDTO.java"): """package com.foodpackaging.dto;
import lombok.Data;
@Data public class PackagingPropertiesDTO {
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
}""",

    os.path.join(src_main_java, "dto", "CreatePackagingMaterialRequest.java"): """package com.foodpackaging.dto;
import lombok.Data;
@Data public class CreatePackagingMaterialRequest {
    private String name;
    private String materialCode;
    private String category;
    private String description;
    private PackagingPropertiesDTO properties;
}""",

    os.path.join(src_main_java, "dto", "RecommendationRequestDTO.java"): """package com.foodpackaging.dto;
import lombok.Data;
@Data public class RecommendationRequestDTO {
    private Long foodCommodityId;
    private Double storageTemperature;
    private Double storageHumidity;
    private Integer requiredShelfLifeDays;
    private Boolean isRefrigerated;
    private Boolean lightExposure;
    private Double costPreference;
    private Double sustainabilityPreference;
    private Double performancePreference;
    private Double shelfLifePreference;
}""",

    os.path.join(src_main_java, "dto", "RecommendationResponseDTO.java"): """package com.foodpackaging.dto;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;
@Data public class RecommendationResponseDTO {
    private Long requestId;
    private String foodCommodityName;
    private String status;
    private List<RecommendationResultDTO> results;
    private LocalDateTime createdAt;
}""",

    os.path.join(src_main_java, "dto", "RecommendationResultDTO.java"): """package com.foodpackaging.dto;
import lombok.Data;
import java.util.List;
@Data public class RecommendationResultDTO {
    private Long materialId;
    private String materialName;
    private String materialCode;
    private Integer rank;
    private Double suitabilityScore;
    private Double performanceScore;
    private Double costScore;
    private Double sustainabilityScore;
    private Double overallScore;
    private String explanation;
    private List<RecommendationFactorDTO> factors;
    private PackagingPropertiesDTO materialProperties;
}""",

    os.path.join(src_main_java, "dto", "RecommendationFactorDTO.java"): """package com.foodpackaging.dto;
import lombok.Data;
@Data public class RecommendationFactorDTO {
    private String factorName;
    private Double factorValue;
    private Double contributionPercentage;
    private String direction;
}""",

    os.path.join(src_main_java, "dto", "RecommendationHistoryDTO.java"): """package com.foodpackaging.dto;
import lombok.Data;
import java.time.LocalDateTime;
@Data public class RecommendationHistoryDTO {
    private Long id;
    private String foodCommodityName;
    private String topMaterial;
    private Double topScore;
    private String status;
    private LocalDateTime createdAt;
}""",

    os.path.join(src_main_java, "dto", "ProducerDashboardDTO.java"): """package com.foodpackaging.dto;
import lombok.Data;
import java.util.List;
@Data public class ProducerDashboardDTO {
    private Long totalRecommendations;
    private Long savedRecommendations;
    private Double avgSuitabilityScore;
    private String mostRecommendedMaterial;
    private List<RecommendationHistoryDTO> recentRecommendations;
}""",

    os.path.join(src_main_java, "dto", "TechnologistDashboardDTO.java"): """package com.foodpackaging.dto;
import lombok.Data;
@Data public class TechnologistDashboardDTO {
    private Long totalFoodCommodities;
    private Long totalPackagingMaterials;
    private Double modelAccuracy;
    private String modelVersion;
    private Long totalRecommendations;
}""",

    os.path.join(src_main_java, "dto", "AdminDashboardDTO.java"): """package com.foodpackaging.dto;
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
}""",

    os.path.join(src_main_java, "dto", "ModelInfoDTO.java"): """package com.foodpackaging.dto;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.Map;
@Data public class ModelInfoDTO {
    private String modelName;
    private String algorithm;
    private String version;
    private LocalDateTime trainingDate;
    private Integer datasetSize;
    private Map<String, Double> metrics;
}""",
    
    os.path.join(src_main_java, "dto", "PageResponse.java"): """package com.foodpackaging.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.List;
@Data @AllArgsConstructor public class PageResponse<T> {
    private List<T> content;
    private int page;
    private int size;
    private long totalElements;
    private int totalPages;
}"""
}

for path, content in files.items():
    write_file(path, content)
print("DTOs generated.")
