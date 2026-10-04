package com.foodpackaging.service;

import com.foodpackaging.dto.CreateFoodCommodityRequest;
import com.foodpackaging.dto.FoodCategoryDTO;
import com.foodpackaging.dto.FoodCommodityDTO;
import com.foodpackaging.dto.FoodPropertiesDTO;
import com.foodpackaging.entity.FoodCategory;
import com.foodpackaging.entity.FoodCommodity;
import com.foodpackaging.entity.FoodProperties;
import com.foodpackaging.exception.ResourceNotFoundException;
import com.foodpackaging.repository.FoodCategoryRepository;
import com.foodpackaging.repository.FoodCommodityRepository;
import com.foodpackaging.repository.FoodPropertiesRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FoodCommodityService {

    private final FoodCommodityRepository foodCommodityRepository;
    private final FoodCategoryRepository categoryRepository;
    private final FoodPropertiesRepository propertiesRepository;

    public FoodCommodityService(
            FoodCommodityRepository foodCommodityRepository,
            FoodCategoryRepository categoryRepository,
            FoodPropertiesRepository propertiesRepository) {
        this.foodCommodityRepository = foodCommodityRepository;
        this.categoryRepository = categoryRepository;
        this.propertiesRepository = propertiesRepository;
    }

    public List<FoodCommodityDTO> getAll() {
        return foodCommodityRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public List<FoodCommodityDTO> search(String query, Long categoryId) {
        List<FoodCommodity> list;
        if (query != null && !query.trim().isEmpty()) {
            list = foodCommodityRepository.findByNameContainingIgnoreCase(query.trim());
        } else if (categoryId != null) {
            list = foodCommodityRepository.findByCategoryId(categoryId);
        } else {
            list = foodCommodityRepository.findAll();
        }
        return list.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public FoodCommodityDTO getById(Long id) {
        FoodCommodity commodity = foodCommodityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Food commodity not found with id: " + id));
        return mapToDTO(commodity);
    }

    public List<FoodCategoryDTO> getCategories() {
        return categoryRepository.findAll().stream().map(c -> {
            FoodCategoryDTO dto = new FoodCategoryDTO();
            dto.setId(c.getId());
            dto.setName(c.getName());
            dto.setDescription(c.getDescription());
            dto.setCommodityCount(c.getFoodCommodities() != null ? c.getFoodCommodities().size() : 0);
            return dto;
        }).collect(Collectors.toList());
    }

    @Transactional
    public FoodCommodityDTO create(CreateFoodCommodityRequest request) {
        FoodCategory category = null;
        if (request.getCategoryId() != null) {
            category = categoryRepository.findById(request.getCategoryId())
                    .orElse(null);
        }

        FoodCommodity commodity = FoodCommodity.builder()
                .name(request.getName())
                .description(request.getDescription())
                .category(category)
                .build();
        commodity = foodCommodityRepository.save(commodity);

        if (request.getProperties() != null) {
            FoodProperties props = mapToEntity(request.getProperties(), commodity);
            props = propertiesRepository.save(props);
            commodity.setFoodProperties(props);
        }

        return mapToDTO(commodity);
    }

    @Transactional
    public FoodCommodityDTO update(Long id, CreateFoodCommodityRequest request) {
        FoodCommodity commodity = foodCommodityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Food commodity not found with id: " + id));

        commodity.setName(request.getName());
        commodity.setDescription(request.getDescription());
        if (request.getCategoryId() != null) {
            FoodCategory category = categoryRepository.findById(request.getCategoryId()).orElse(null);
            commodity.setCategory(category);
        }

        if (request.getProperties() != null) {
            FoodProperties props = commodity.getFoodProperties();
            if (props == null) {
                props = new FoodProperties();
                props.setFoodCommodity(commodity);
            }
            updatePropertiesFromDTO(props, request.getProperties());
            props = propertiesRepository.save(props);
            commodity.setFoodProperties(props);
        }

        commodity = foodCommodityRepository.save(commodity);
        return mapToDTO(commodity);
    }

    @Transactional
    public void delete(Long id) {
        if (!foodCommodityRepository.existsById(id)) {
            throw new ResourceNotFoundException("Food commodity not found with id: " + id);
        }
        foodCommodityRepository.deleteById(id);
    }

    public FoodCommodityDTO mapToDTO(FoodCommodity commodity) {
        FoodCommodityDTO dto = new FoodCommodityDTO();
        dto.setId(commodity.getId());
        dto.setName(commodity.getName());
        dto.setDescription(commodity.getDescription());
        dto.setImageUrl(commodity.getImageUrl());
        if (commodity.getCategory() != null) {
            dto.setCategoryId(commodity.getCategory().getId());
            dto.setCategoryName(commodity.getCategory().getName());
        }

        if (commodity.getFoodProperties() != null) {
            FoodProperties p = commodity.getFoodProperties();
            FoodPropertiesDTO pdto = new FoodPropertiesDTO();
            pdto.setMoistureContent(p.getMoistureContent());
            pdto.setPhLevel(p.getPhLevel());
            pdto.setFatContent(p.getFatContent());
            pdto.setProteinContent(p.getProteinContent());
            pdto.setCarbohydrateContent(p.getCarbohydrateContent());
            pdto.setRespirationRate(p.getRespirationRate());
            pdto.setOxygenSensitivity(p.getOxygenSensitivity());
            pdto.setMoistureSensitivity(p.getMoistureSensitivity());
            pdto.setLightSensitivity(p.getLightSensitivity());
            pdto.setMicrobialSensitivity(p.getMicrobialSensitivity());
            pdto.setIsPerishable(p.getIsPerishable());
            dto.setProperties(pdto);
        }
        return dto;
    }

    private FoodProperties mapToEntity(FoodPropertiesDTO pdto, FoodCommodity commodity) {
        FoodProperties p = new FoodProperties();
        p.setFoodCommodity(commodity);
        updatePropertiesFromDTO(p, pdto);
        return p;
    }

    private void updatePropertiesFromDTO(FoodProperties p, FoodPropertiesDTO pdto) {
        p.setMoistureContent(pdto.getMoistureContent());
        p.setPhLevel(pdto.getPhLevel());
        p.setFatContent(pdto.getFatContent());
        p.setProteinContent(pdto.getProteinContent());
        p.setCarbohydrateContent(pdto.getCarbohydrateContent());
        p.setRespirationRate(pdto.getRespirationRate());
        p.setOxygenSensitivity(pdto.getOxygenSensitivity());
        p.setMoistureSensitivity(pdto.getMoistureSensitivity());
        p.setLightSensitivity(pdto.getLightSensitivity());
        p.setMicrobialSensitivity(pdto.getMicrobialSensitivity());
        p.setIsPerishable(pdto.getIsPerishable());
    }
}
