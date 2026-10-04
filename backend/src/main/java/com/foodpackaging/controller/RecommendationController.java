package com.foodpackaging.controller;

import com.foodpackaging.dto.*;
import com.foodpackaging.security.UserPrincipal;
import com.foodpackaging.service.RecommendationService;
import com.foodpackaging.service.SavedRecommendationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/recommendations")
public class RecommendationController {

    private final RecommendationService recommendationService;
    private final SavedRecommendationService savedRecommendationService;

    public RecommendationController(RecommendationService recommendationService,
                                    SavedRecommendationService savedRecommendationService) {
        this.recommendationService = recommendationService;
        this.savedRecommendationService = savedRecommendationService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<RecommendationResponseDTO>> create(
            @AuthenticationPrincipal UserPrincipal user,
            @Valid @RequestBody RecommendationRequestDTO request) {
        RecommendationResponseDTO result = recommendationService.createRecommendation(user.getId(), request);
        return ResponseEntity.ok(new ApiResponse<>(true, "Recommendation generated successfully", result, LocalDateTime.now().toString()));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<RecommendationResponseDTO>>> getUserRecommendations(
            @AuthenticationPrincipal UserPrincipal user) {
        List<RecommendationResponseDTO> recommendations = recommendationService.getUserRecommendations(user.getId());
        return ResponseEntity.ok(new ApiResponse<>(true, "Recommendations retrieved", recommendations, LocalDateTime.now().toString()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<RecommendationResponseDTO>> getRecommendation(
            @PathVariable Long id) {
        RecommendationResponseDTO recommendation = recommendationService.getRecommendation(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Recommendation retrieved", recommendation, LocalDateTime.now().toString()));
    }

    @GetMapping("/history")
    public ResponseEntity<ApiResponse<List<RecommendationHistoryDTO>>> getHistory(
            @AuthenticationPrincipal UserPrincipal user) {
        List<RecommendationHistoryDTO> history = recommendationService.getUserHistory(user.getId());
        return ResponseEntity.ok(new ApiResponse<>(true, "History retrieved", history, LocalDateTime.now().toString()));
    }

    @PostMapping("/{id}/save")
    public ResponseEntity<ApiResponse<String>> saveRecommendation(
            @AuthenticationPrincipal UserPrincipal user,
            @PathVariable Long id,
            @RequestBody(required = false) java.util.Map<String, String> body) {
        String notes = body != null ? body.getOrDefault("notes", "") : "";
        savedRecommendationService.save(user.getId(), id, notes);
        return ResponseEntity.ok(new ApiResponse<>(true, "Recommendation saved", "Saved successfully", LocalDateTime.now().toString()));
    }

    @DeleteMapping("/{id}/save")
    public ResponseEntity<ApiResponse<String>> unsaveRecommendation(
            @AuthenticationPrincipal UserPrincipal user,
            @PathVariable Long id) {
        savedRecommendationService.unsave(user.getId(), id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Recommendation unsaved", "Removed from saved", LocalDateTime.now().toString()));
    }

    @PostMapping("/{id}/unsave")
    public ResponseEntity<ApiResponse<String>> unsaveRecommendationPost(
            @AuthenticationPrincipal UserPrincipal user,
            @PathVariable Long id) {
        savedRecommendationService.unsave(user.getId(), id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Recommendation unsaved", "Removed from saved", LocalDateTime.now().toString()));
    }
}
