package com.foodpackaging.repository;
import com.foodpackaging.entity.FoodCommodity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
@Repository public interface FoodCommodityRepository extends JpaRepository<FoodCommodity, Long> {
    List<FoodCommodity> findByCategoryId(Long categoryId);
    List<FoodCommodity> findByNameContainingIgnoreCase(String name);
}
