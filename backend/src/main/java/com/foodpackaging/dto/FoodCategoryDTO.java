package com.foodpackaging.dto;
import lombok.Data;
@Data public class FoodCategoryDTO {
    private Long id;
    private String name;
    private String description;
    private Integer commodityCount;
}
