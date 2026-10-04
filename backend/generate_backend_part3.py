import os

base_dir = r"c:\Users\Vijayakrishnan.R\OneDrive\Desktop\Food tech\ai-food-packaging\backend"
src_main_java = os.path.join(base_dir, "src", "main", "java", "com", "foodpackaging")

def write_file(path, content):
    os.makedirs(os.path.dirname(path), exist_ok=True)
    with open(path, 'w', encoding='utf-8') as f:
        f.write(content.strip() + '\n')

files = {
    # ENTITIES
    os.path.join(src_main_java, "entity", "FoodCategory.java"): """package com.foodpackaging.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "food_categories")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FoodCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String description;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    private List<FoodCommodity> foodCommodities;
    
    @PrePersist
    protected void onCreate() { createdAt = LocalDateTime.now(); }
}
""",
    os.path.join(src_main_java, "entity", "FoodCommodity.java"): """package com.foodpackaging.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "food_commodities")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FoodCommodity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private FoodCategory category;

    private String description;
    private String imageUrl;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToOne(mappedBy = "foodCommodity", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private FoodProperties foodProperties;
    
    @PrePersist
    protected void onCreate() { createdAt = LocalDateTime.now(); updatedAt = LocalDateTime.now(); }
    @PreUpdate
    protected void onUpdate() { updatedAt = LocalDateTime.now(); }
}
""",
    os.path.join(src_main_java, "entity", "FoodProperties.java"): """package com.foodpackaging.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "food_properties")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FoodProperties {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "food_commodity_id")
    private FoodCommodity foodCommodity;

    private Double moistureContent;
    private Double phLevel;
    private Double fatContent;
    private Double proteinContent;
    private Double carbohydrateContent;
    private Double respirationRate;
    private String oxygenSensitivity;
    private String moistureSensitivity;
    private String lightSensitivity;
    private String microbialSensitivity;
    private Boolean isPerishable;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() { createdAt = LocalDateTime.now(); updatedAt = LocalDateTime.now(); }
    @PreUpdate
    protected void onUpdate() { updatedAt = LocalDateTime.now(); }
}
"""
}

for path, content in files.items():
    write_file(path, content)
print("Entities generated.")
