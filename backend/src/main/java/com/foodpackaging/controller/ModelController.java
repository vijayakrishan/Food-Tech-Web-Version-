package com.foodpackaging.controller;

import com.foodpackaging.dto.ApiResponse;
import com.foodpackaging.dto.ModelInfoDTO;
import com.foodpackaging.service.ModelService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/model")
public class ModelController {

    private final ModelService modelService;

    public ModelController(ModelService modelService) {
        this.modelService = modelService;
    }

    @GetMapping("/info")
    public ResponseEntity<ApiResponse<ModelInfoDTO>> getModelInfo() {
        ModelInfoDTO info = modelService.getModelInfo();
        return ResponseEntity.ok(new ApiResponse<>(true, "Model info retrieved", info, LocalDateTime.now().toString()));
    }

    @GetMapping("/metrics")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getModelMetrics() {
        Map<String, Object> metrics = modelService.getModelMetrics();
        return ResponseEntity.ok(new ApiResponse<>(true, "Model metrics retrieved", metrics, LocalDateTime.now().toString()));
    }
}
