package com.foodpackaging.repository;
import com.foodpackaging.entity.FoodCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository public interface FoodCategoryRepository extends JpaRepository<FoodCategory, Long> {}
