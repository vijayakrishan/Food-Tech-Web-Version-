package com.foodpackaging.repository;
import com.foodpackaging.entity.FoodProperties;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
@Repository public interface FoodPropertiesRepository extends JpaRepository<FoodProperties, Long> {
    Optional<FoodProperties> findByFoodCommodityId(Long foodCommodityId);
}
