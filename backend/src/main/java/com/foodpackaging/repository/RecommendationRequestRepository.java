package com.foodpackaging.repository;
import com.foodpackaging.entity.RecommendationRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
@Repository public interface RecommendationRequestRepository extends JpaRepository<RecommendationRequest, Long> {
    List<RecommendationRequest> findByUserIdOrderByCreatedAtDesc(Long userId);
    Long countByUserId(Long userId);
    List<RecommendationRequest> findByStatus(String status);
}
