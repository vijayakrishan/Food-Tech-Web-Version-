package com.foodpackaging.dto;
import lombok.Data;
@Data public class PackagingMaterialDTO {
    private Long id;
    private String name;
    private String materialCode;
    private String category;
    private String description;
    private PackagingPropertiesDTO properties;
}
