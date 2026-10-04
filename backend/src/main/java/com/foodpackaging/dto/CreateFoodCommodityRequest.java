package com.foodpackaging.dto;
import lombok.Data;
@Data public class CreateFoodCommodityRequest {
    private String name;
    private Long categoryId;
    private String description;
    private FoodPropertiesDTO properties;
}
