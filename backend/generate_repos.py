import os

base_dir = r"c:\Users\Vijayakrishnan.R\OneDrive\Desktop\Food tech\ai-food-packaging\backend"
src_main_java = os.path.join(base_dir, "src", "main", "java", "com", "foodpackaging")

def write_file(path, content):
    os.makedirs(os.path.dirname(path), exist_ok=True)
    with open(path, 'w', encoding='utf-8') as f:
        f.write(content.strip() + '\n')

files = {
    os.path.join(src_main_java, "repository", "FoodCategoryRepository.java"): """package com.foodpackaging.repository;
import com.foodpackaging.entity.FoodCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository public interface FoodCategoryRepository extends JpaRepository<FoodCategory, Long> {}""",

    os.path.join(src_main_java, "repository", "FoodCommodityRepository.java"): """package com.foodpackaging.repository;
import com.foodpackaging.entity.FoodCommodity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
@Repository public interface FoodCommodityRepository extends JpaRepository<FoodCommodity, Long> {
    List<FoodCommodity> findByCategoryId(Long categoryId);
    List<FoodCommodity> findByNameContainingIgnoreCase(String name);
}""",

    os.path.join(src_main_java, "repository", "FoodPropertiesRepository.java"): """package com.foodpackaging.repository;
import com.foodpackaging.entity.FoodProperties;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
@Repository public interface FoodPropertiesRepository extends JpaRepository<FoodProperties, Long> {
    Optional<FoodProperties> findByFoodCommodityId(Long foodCommodityId);
}""",

    os.path.join(src_main_java, "repository", "PackagingMaterialRepository.java"): """package com.foodpackaging.repository;
import com.foodpackaging.entity.PackagingMaterial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
@Repository public interface PackagingMaterialRepository extends JpaRepository<PackagingMaterial, Long> {
    List<PackagingMaterial> findByCategory(String category);
    Optional<PackagingMaterial> findByMaterialCode(String materialCode);
}""",

    os.path.join(src_main_java, "repository", "PackagingPropertiesRepository.java"): """package com.foodpackaging.repository;
import com.foodpackaging.entity.PackagingProperties;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
@Repository public interface PackagingPropertiesRepository extends JpaRepository<PackagingProperties, Long> {
    Optional<PackagingProperties> findByPackagingMaterialId(Long packagingMaterialId);
}""",

    os.path.join(src_main_java, "repository", "RecommendationRequestRepository.java"): """package com.foodpackaging.repository;
import com.foodpackaging.entity.RecommendationRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
@Repository public interface RecommendationRequestRepository extends JpaRepository<RecommendationRequest, Long> {
    List<RecommendationRequest> findByUserIdOrderByCreatedAtDesc(Long userId);
    Long countByUserId(Long userId);
    List<RecommendationRequest> findByStatus(String status);
}""",

    os.path.join(src_main_java, "repository", "RecommendationResultRepository.java"): """package com.foodpackaging.repository;
import com.foodpackaging.entity.RecommendationResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
@Repository public interface RecommendationResultRepository extends JpaRepository<RecommendationResult, Long> {
    List<RecommendationResult> findByRecommendationRequestId(Long requestId);
}""",

    os.path.join(src_main_java, "repository", "RecommendationFactorRepository.java"): """package com.foodpackaging.repository;
import com.foodpackaging.entity.RecommendationFactor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
@Repository public interface RecommendationFactorRepository extends JpaRepository<RecommendationFactor, Long> {
    List<RecommendationFactor> findByRecommendationResultId(Long resultId);
}""",

    os.path.join(src_main_java, "repository", "SavedRecommendationRepository.java"): """package com.foodpackaging.repository;
import com.foodpackaging.entity.SavedRecommendation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
@Repository public interface SavedRecommendationRepository extends JpaRepository<SavedRecommendation, Long> {
    List<SavedRecommendation> findByUserId(Long userId);
    Boolean existsByUserIdAndRecommendationRequestId(Long userId, Long requestId);
}""",

    os.path.join(src_main_java, "repository", "ModelVersionRepository.java"): """package com.foodpackaging.repository;
import com.foodpackaging.entity.ModelVersion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
@Repository public interface ModelVersionRepository extends JpaRepository<ModelVersion, Long> {
    Optional<ModelVersion> findByIsActiveTrue();
}""",

    os.path.join(src_main_java, "repository", "AuditLogRepository.java"): """package com.foodpackaging.repository;
import com.foodpackaging.entity.AuditLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
    Page<AuditLog> findAll(Pageable pageable);
}"""
}

for path, content in files.items():
    write_file(path, content)
print("Repositories generated.")
