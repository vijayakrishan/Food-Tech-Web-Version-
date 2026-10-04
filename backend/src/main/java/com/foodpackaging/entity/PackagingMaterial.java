package com.foodpackaging.entity;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity @Table(name = "packaging_materials")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class PackagingMaterial {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(nullable = false) private String name;
    @Column(nullable = false, unique = true) private String materialCode;
    private String category;
    private String description;
    @Column(name = "created_at") private LocalDateTime createdAt;
    @Column(name = "updated_at") private LocalDateTime updatedAt;
    @OneToOne(mappedBy = "packagingMaterial", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private PackagingProperties packagingProperties;
    @PrePersist protected void onCreate() { createdAt = LocalDateTime.now(); updatedAt = LocalDateTime.now(); }
    @PreUpdate protected void onUpdate() { updatedAt = LocalDateTime.now(); }
}
