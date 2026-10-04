package com.foodpackaging.controller;

import com.foodpackaging.dto.AdminDashboardDTO;
import com.foodpackaging.dto.ApiResponse;
import com.foodpackaging.dto.ProducerDashboardDTO;
import com.foodpackaging.dto.TechnologistDashboardDTO;
import com.foodpackaging.security.UserPrincipal;
import com.foodpackaging.service.DashboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/producer")
    public ResponseEntity<ApiResponse<ProducerDashboardDTO>> getProducerDashboard(
            @AuthenticationPrincipal UserPrincipal user) {
        ProducerDashboardDTO dto = dashboardService.getProducerDashboard(user.getId());
        return ResponseEntity.ok(new ApiResponse<>(true, "Producer dashboard retrieved", dto, LocalDateTime.now().toString()));
    }

    @GetMapping("/technologist")
    public ResponseEntity<ApiResponse<TechnologistDashboardDTO>> getTechnologistDashboard() {
        TechnologistDashboardDTO dto = dashboardService.getTechnologistDashboard();
        return ResponseEntity.ok(new ApiResponse<>(true, "Technologist dashboard retrieved", dto, LocalDateTime.now().toString()));
    }

    @GetMapping("/admin")
    public ResponseEntity<ApiResponse<AdminDashboardDTO>> getAdminDashboard() {
        AdminDashboardDTO dto = dashboardService.getAdminDashboard();
        return ResponseEntity.ok(new ApiResponse<>(true, "Admin dashboard retrieved", dto, LocalDateTime.now().toString()));
    }
}
