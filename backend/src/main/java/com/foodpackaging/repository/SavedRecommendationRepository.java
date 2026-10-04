package com.foodpackaging.repository;

import com.foodpackaging.entity.SavedRecommendation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SavedRecommendationRepository extends JpaRepository<SavedRecommendation, Long> {
    List<SavedRecommendation> findByUserId(Long userId);
    Boolean existsByUserIdAndRecommendationRequestId(Long userId, Long requestId);
    Optional<SavedRecommendation> findByUserIdAndRecommendationRequestId(Long userId, Long requestId);
    void deleteByUserIdAndRecommendationRequestId(Long userId, Long requestId);
    Long countByUserId(Long userId);
}
