package com.foodpackaging.repository;
import com.foodpackaging.entity.PackagingProperties;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
@Repository public interface PackagingPropertiesRepository extends JpaRepository<PackagingProperties, Long> {
    Optional<PackagingProperties> findByPackagingMaterialId(Long packagingMaterialId);
}
