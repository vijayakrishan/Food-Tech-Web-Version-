package com.foodpackaging.repository;
import com.foodpackaging.entity.PackagingMaterial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
@Repository public interface PackagingMaterialRepository extends JpaRepository<PackagingMaterial, Long> {
    List<PackagingMaterial> findByCategory(String category);
    Optional<PackagingMaterial> findByMaterialCode(String materialCode);
}
