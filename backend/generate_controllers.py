import os

base_dir = r"c:\Users\Vijayakrishnan.R\OneDrive\Desktop\Food tech\ai-food-packaging\backend"
src_main_java = os.path.join(base_dir, "src", "main", "java", "com", "foodpackaging")

def write_file(path, content):
    os.makedirs(os.path.dirname(path), exist_ok=True)
    with open(path, 'w', encoding='utf-8') as f:
        f.write(content.strip() + '\n')

files = {
    os.path.join(src_main_java, "controller", "AuthController.java"): """package com.foodpackaging.controller;
import com.foodpackaging.dto.*;
import com.foodpackaging.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;
    public AuthController(AuthService authService) { this.authService = authService; }
    
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(ApiResponse.<AuthResponse>builder().success(true).data(authService.login(request)).build());
    }
    
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthResponse>> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(ApiResponse.<AuthResponse>builder().success(true).data(authService.register(request)).build());
    }
}""",

    os.path.join(src_main_java, "controller", "FoodCommodityController.java"): """package com.foodpackaging.controller;
import com.foodpackaging.dto.ApiResponse;
import com.foodpackaging.entity.FoodCommodity;
import com.foodpackaging.service.FoodCommodityService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/foods")
public class FoodCommodityController {
    private final FoodCommodityService service;
    public FoodCommodityController(FoodCommodityService service) { this.service = service; }
    
    @GetMapping("/")
    public ResponseEntity<ApiResponse<List<FoodCommodity>>> getAll() {
        return ResponseEntity.ok(ApiResponse.<List<FoodCommodity>>builder().success(true).data(service.getAll()).build());
    }
}""",

    os.path.join(src_main_java, "controller", "PackagingMaterialController.java"): """package com.foodpackaging.controller;
import com.foodpackaging.dto.ApiResponse;
import com.foodpackaging.entity.PackagingMaterial;
import com.foodpackaging.service.PackagingMaterialService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/packaging-materials")
public class PackagingMaterialController {
    private final PackagingMaterialService service;
    public PackagingMaterialController(PackagingMaterialService service) { this.service = service; }
    
    @GetMapping("/")
    public ResponseEntity<ApiResponse<List<PackagingMaterial>>> getAll() {
        return ResponseEntity.ok(ApiResponse.<List<PackagingMaterial>>builder().success(true).data(service.getAll()).build());
    }
}""",

    os.path.join(src_main_java, "controller", "RecommendationController.java"): """package com.foodpackaging.controller;
import com.foodpackaging.dto.*;
import com.foodpackaging.security.UserPrincipal;
import com.foodpackaging.service.RecommendationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/recommendations")
public class RecommendationController {
    private final RecommendationService service;
    public RecommendationController(RecommendationService service) { this.service = service; }
    
    @PostMapping("/")
    public ResponseEntity<ApiResponse<RecommendationResponseDTO>> create(@AuthenticationPrincipal UserPrincipal user, @RequestBody RecommendationRequestDTO request) {
        return ResponseEntity.ok(ApiResponse.<RecommendationResponseDTO>builder().success(true).data(service.createRecommendation(user.getId(), request)).build());
    }
}""",

    os.path.join(src_main_java, "controller", "DashboardController.java"): """package com.foodpackaging.controller;
import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/api/dashboard") public class DashboardController { }""",

    os.path.join(src_main_java, "controller", "ModelController.java"): """package com.foodpackaging.controller;
import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/api/model") public class ModelController { }""",

    os.path.join(src_main_java, "controller", "AdminController.java"): """package com.foodpackaging.controller;
import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/api/admin") public class AdminController { }"""
}

for path, content in files.items():
    write_file(path, content)
print("Controllers generated.")
