package com.foodpackaging.dto;
import lombok.Data;
@Data public class FoodCommodityDTO {
    private Long id;
    private String name;
    private Long categoryId;
    private String categoryName;
    private String description;
    private String imageUrl;
    private FoodPropertiesDTO properties;
}
