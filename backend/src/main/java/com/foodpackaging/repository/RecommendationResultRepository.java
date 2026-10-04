package com.foodpackaging.repository;

import com.foodpackaging.entity.RecommendationResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecommendationResultRepository extends JpaRepository<RecommendationResult, Long> {
    List<RecommendationResult> findByRecommendationRequestId(Long requestId);
    List<RecommendationResult> findByRecommendationRequestIdOrderByRankAsc(Long requestId);

    @Query("SELECT r.packagingMaterial.name, COUNT(r) FROM RecommendationResult r WHERE r.rank = 1 GROUP BY r.packagingMaterial.name ORDER BY COUNT(r) DESC")
    List<Object[]> findMostRecommendedMaterials();

    @Query("SELECT AVG(r.suitabilityScore) FROM RecommendationResult r WHERE r.recommendationRequest.user.id = :userId")
    Double getAverageSuitabilityScoreForUser(Long userId);

    @Query("SELECT AVG(r.suitabilityScore) FROM RecommendationResult r")
    Double getOverallAverageSuitabilityScore();
}
