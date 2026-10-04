import os

base_dir = r"c:\Users\Vijayakrishnan.R\OneDrive\Desktop\Food tech\ai-food-packaging\backend"
src_main_java = os.path.join(base_dir, "src", "main", "java", "com", "foodpackaging")

def write_file(path, content):
    os.makedirs(os.path.dirname(path), exist_ok=True)
    with open(path, 'w', encoding='utf-8') as f:
        f.write(content.strip() + '\n')

files = {
    os.path.join(src_main_java, "service", "AuthService.java"): """package com.foodpackaging.service;
import com.foodpackaging.dto.*;
import com.foodpackaging.entity.*;
import com.foodpackaging.exception.BadRequestException;
import com.foodpackaging.repository.RoleRepository;
import com.foodpackaging.repository.UserRepository;
import com.foodpackaging.security.JwtTokenProvider;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;

    public AuthService(AuthenticationManager authenticationManager, UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, JwtTokenProvider tokenProvider) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
    }

    public AuthResponse login(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.generateToken(authentication);
        User user = userRepository.findByUsername(loginRequest.getUsername()).orElseThrow(() -> new BadRequestException("User not found"));
        return AuthResponse.builder().token(jwt).id(user.getId()).username(user.getUsername()).email(user.getEmail()).role(user.getRole().getName().name()).build();
    }

    @Transactional
    public AuthResponse register(RegisterRequest registerRequest) {
        if (userRepository.existsByUsername(registerRequest.getUsername())) { throw new BadRequestException("Username is already taken!"); }
        if (userRepository.existsByEmail(registerRequest.getEmail())) { throw new BadRequestException("Email is already in use!"); }
        Role userRole = roleRepository.findByName(ERole.valueOf(registerRequest.getRole() != null ? registerRequest.getRole() : "ROLE_PRODUCER"))
                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
        User user = User.builder().username(registerRequest.getUsername()).email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword())).firstName(registerRequest.getFirstName())
                .lastName(registerRequest.getLastName()).phone(registerRequest.getPhone()).organization(registerRequest.getOrganization())
                .role(userRole).isActive(true).build();
        userRepository.save(user);
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(registerRequest.getUsername(), registerRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.generateToken(authentication);
        return AuthResponse.builder().token(jwt).id(user.getId()).username(user.getUsername()).email(user.getEmail()).role(user.getRole().getName().name()).build();
    }
}""",

    os.path.join(src_main_java, "service", "UserService.java"): """package com.foodpackaging.service;
import com.foodpackaging.entity.User;
import com.foodpackaging.exception.ResourceNotFoundException;
import com.foodpackaging.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    public UserService(UserRepository userRepository) { this.userRepository = userRepository; }
    public List<User> getAllUsers() { return userRepository.findAll(); }
    public User getUserById(Long id) { return userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found")); }
    @Transactional
    public User toggleUserActive(Long id) {
        User user = getUserById(id);
        user.setActive(!user.isActive());
        return userRepository.save(user);
    }
}""",

    os.path.join(src_main_java, "service", "MLServiceClient.java"): """package com.foodpackaging.service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.Map;
import java.util.List;

@Service
public class MLServiceClient {
    private final RestTemplate restTemplate;
    @Value("${app.ml-service.url}")
    private String mlServiceUrl;

    public MLServiceClient(RestTemplate restTemplate) { this.restTemplate = restTemplate; }

    public Map<String, Object> predict(Map<String, Object> payload) {
        return restTemplate.postForObject(mlServiceUrl + "/predict", payload, Map.class);
    }

    public List<Map<String, Object>> batchPredict(List<Map<String, Object>> payloads) {
        return restTemplate.postForObject(mlServiceUrl + "/batch-predict", payloads, List.class);
    }

    public Map<String, Object> explain(Map<String, Object> payload) {
        return restTemplate.postForObject(mlServiceUrl + "/explain", payload, Map.class);
    }

    public Map<String, Object> getModelInfo() {
        return restTemplate.getForObject(mlServiceUrl + "/model-info", Map.class);
    }

    public Map<String, Object> getHealth() {
        return restTemplate.getForObject(mlServiceUrl + "/health", Map.class);
    }
}""",

    os.path.join(src_main_java, "service", "FoodCommodityService.java"): """package com.foodpackaging.service;
import com.foodpackaging.entity.FoodCommodity;
import com.foodpackaging.repository.FoodCommodityRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class FoodCommodityService {
    private final FoodCommodityRepository foodCommodityRepository;
    public FoodCommodityService(FoodCommodityRepository foodCommodityRepository) { this.foodCommodityRepository = foodCommodityRepository; }
    public List<FoodCommodity> getAll() { return foodCommodityRepository.findAll(); }
    public FoodCommodity getById(Long id) { return foodCommodityRepository.findById(id).orElseThrow(); }
}""",

    os.path.join(src_main_java, "service", "PackagingMaterialService.java"): """package com.foodpackaging.service;
import com.foodpackaging.entity.PackagingMaterial;
import com.foodpackaging.repository.PackagingMaterialRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PackagingMaterialService {
    private final PackagingMaterialRepository repository;
    public PackagingMaterialService(PackagingMaterialRepository repository) { this.repository = repository; }
    public List<PackagingMaterial> getAll() { return repository.findAll(); }
    public PackagingMaterial getById(Long id) { return repository.findById(id).orElseThrow(); }
}"""
}

for path, content in files.items():
    write_file(path, content)
print("Services part 1 generated.")
