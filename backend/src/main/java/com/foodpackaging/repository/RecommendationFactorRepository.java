package com.foodpackaging.repository;
import com.foodpackaging.entity.RecommendationFactor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
@Repository public interface RecommendationFactorRepository extends JpaRepository<RecommendationFactor, Long> {
    List<RecommendationFactor> findByRecommendationResultId(Long resultId);
}
