package com.foodpackaging.controller;

import com.foodpackaging.dto.ApiResponse;
import com.foodpackaging.dto.RecommendationResponseDTO;
import com.foodpackaging.entity.AuditLog;
import com.foodpackaging.entity.User;
import com.foodpackaging.repository.AuditLogRepository;
import com.foodpackaging.repository.RecommendationRequestRepository;
import com.foodpackaging.service.RecommendationService;
import com.foodpackaging.service.UserService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final UserService userService;
    private final AuditLogRepository auditLogRepository;
    private final RecommendationRequestRepository recommendationRequestRepository;
    private final RecommendationService recommendationService;

    public AdminController(
            UserService userService,
            AuditLogRepository auditLogRepository,
            RecommendationRequestRepository recommendationRequestRepository,
            RecommendationService recommendationService) {
        this.userService = userService;
        this.auditLogRepository = auditLogRepository;
        this.recommendationRequestRepository = recommendationRequestRepository;
        this.recommendationService = recommendationService;
    }

    @GetMapping("/users")
    public ResponseEntity<ApiResponse<List<User>>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(new ApiResponse<>(true, "Users retrieved", users, LocalDateTime.now().toString()));
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<ApiResponse<User>> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "User retrieved", user, LocalDateTime.now().toString()));
    }

    @PutMapping("/users/{id}/toggle-active")
    public ResponseEntity<ApiResponse<User>> toggleUserActive(@PathVariable Long id) {
        User updated = userService.toggleUserActive(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "User active status updated", updated, LocalDateTime.now().toString()));
    }

    @PutMapping("/users/{id}/role")
    public ResponseEntity<ApiResponse<User>> changeUserRole(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {
        String role = body.getOrDefault("role", "ROLE_PRODUCER");
        User updated = userService.changeUserRole(id, role);
        return ResponseEntity.ok(new ApiResponse<>(true, "User role updated successfully", updated, LocalDateTime.now().toString()));
    }

    @GetMapping("/audit-logs")
    public ResponseEntity<ApiResponse<List<AuditLog>>> getAuditLogs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        List<AuditLog> logs = auditLogRepository.findAll(PageRequest.of(page, size, Sort.by("createdAt").descending())).getContent();
        return ResponseEntity.ok(new ApiResponse<>(true, "Audit logs retrieved", logs, LocalDateTime.now().toString()));
    }

    @GetMapping("/recommendations")
    public ResponseEntity<ApiResponse<List<RecommendationResponseDTO>>> getAllRecommendations() {
        List<RecommendationResponseDTO> list = recommendationRequestRepository.findAll().stream()
                .map(r -> recommendationService.getRecommendation(r.getId()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(new ApiResponse<>(true, "All recommendations retrieved", list, LocalDateTime.now().toString()));
    }
}
