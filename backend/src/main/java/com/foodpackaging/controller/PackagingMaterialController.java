package com.foodpackaging.controller;

import com.foodpackaging.dto.ApiResponse;
import com.foodpackaging.dto.CreatePackagingMaterialRequest;
import com.foodpackaging.dto.PackagingMaterialDTO;
import com.foodpackaging.service.PackagingMaterialService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/packaging-materials")
public class PackagingMaterialController {

    private final PackagingMaterialService service;

    public PackagingMaterialController(PackagingMaterialService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<PackagingMaterialDTO>>> getAll(
            @RequestParam(required = false) String category) {
        List<PackagingMaterialDTO> list = service.getAll(category);
        return ResponseEntity.ok(new ApiResponse<>(true, "Packaging materials retrieved", list, LocalDateTime.now().toString()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PackagingMaterialDTO>> getById(@PathVariable Long id) {
        PackagingMaterialDTO dto = service.getById(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Packaging material retrieved", dto, LocalDateTime.now().toString()));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<PackagingMaterialDTO>> create(@Valid @RequestBody CreatePackagingMaterialRequest request) {
        PackagingMaterialDTO created = service.create(request);
        return ResponseEntity.ok(new ApiResponse<>(true, "Packaging material created successfully", created, LocalDateTime.now().toString()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<PackagingMaterialDTO>> update(
            @PathVariable Long id,
            @Valid @RequestBody CreatePackagingMaterialRequest request) {
        PackagingMaterialDTO updated = service.update(id, request);
        return ResponseEntity.ok(new ApiResponse<>(true, "Packaging material updated successfully", updated, LocalDateTime.now().toString()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Packaging material deleted successfully", null, LocalDateTime.now().toString()));
    }
}
