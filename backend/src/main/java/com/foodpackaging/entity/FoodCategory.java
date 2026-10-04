package com.foodpackaging.entity;

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
