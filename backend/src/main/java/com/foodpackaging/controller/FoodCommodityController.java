package com.foodpackaging.controller;

import com.foodpackaging.dto.ApiResponse;
import com.foodpackaging.dto.CreateFoodCommodityRequest;
import com.foodpackaging.dto.FoodCategoryDTO;
import com.foodpackaging.dto.FoodCommodityDTO;
import com.foodpackaging.service.FoodCommodityService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/foods")
public class FoodCommodityController {

    private final FoodCommodityService service;

    public FoodCommodityController(FoodCommodityService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<FoodCommodityDTO>>> getAll(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Long categoryId) {
        List<FoodCommodityDTO> list;
        if ((search != null && !search.isEmpty()) || categoryId != null) {
            list = service.search(search, categoryId);
        } else {
            list = service.getAll();
        }
        return ResponseEntity.ok(new ApiResponse<>(true, "Food commodities retrieved", list, LocalDateTime.now().toString()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<FoodCommodityDTO>> getById(@PathVariable Long id) {
        FoodCommodityDTO dto = service.getById(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Food commodity retrieved", dto, LocalDateTime.now().toString()));
    }

    @GetMapping("/categories")
    public ResponseEntity<ApiResponse<List<FoodCategoryDTO>>> getCategories() {
        List<FoodCategoryDTO> list = service.getCategories();
        return ResponseEntity.ok(new ApiResponse<>(true, "Food categories retrieved", list, LocalDateTime.now().toString()));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<FoodCommodityDTO>> create(@Valid @RequestBody CreateFoodCommodityRequest request) {
        FoodCommodityDTO created = service.create(request);
        return ResponseEntity.ok(new ApiResponse<>(true, "Food commodity created successfully", created, LocalDateTime.now().toString()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<FoodCommodityDTO>> update(
            @PathVariable Long id,
            @Valid @RequestBody CreateFoodCommodityRequest request) {
        FoodCommodityDTO updated = service.update(id, request);
        return ResponseEntity.ok(new ApiResponse<>(true, "Food commodity updated successfully", updated, LocalDateTime.now().toString()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Food commodity deleted successfully", null, LocalDateTime.now().toString()));
    }
}
