import os

base_dir = r"c:\Users\Vijayakrishnan.R\OneDrive\Desktop\Food tech\ai-food-packaging\backend"
src_main_java = os.path.join(base_dir, "src", "main", "java", "com", "foodpackaging")

def write_file(path, content):
    os.makedirs(os.path.dirname(path), exist_ok=True)
    with open(path, 'w', encoding='utf-8') as f:
        f.write(content.strip() + '\n')

files = {
    os.path.join(src_main_java, "service", "RecommendationService.java"): """package com.foodpackaging.service;
import com.foodpackaging.dto.*;
import com.foodpackaging.entity.*;
import com.foodpackaging.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class RecommendationService {
    private final RecommendationRequestRepository requestRepository;
    private final RecommendationResultRepository resultRepository;
    private final RecommendationFactorRepository factorRepository;
    private final PackagingMaterialRepository materialRepository;
    private final FoodCommodityRepository foodRepository;
    private final UserRepository userRepository;
    private final MLServiceClient mlServiceClient;

    public RecommendationService(RecommendationRequestRepository requestRepository, RecommendationResultRepository resultRepository, RecommendationFactorRepository factorRepository, PackagingMaterialRepository materialRepository, FoodCommodityRepository foodRepository, UserRepository userRepository, MLServiceClient mlServiceClient) {
        this.requestRepository = requestRepository;
        this.resultRepository = resultRepository;
        this.factorRepository = factorRepository;
        this.materialRepository = materialRepository;
        this.foodRepository = foodRepository;
        this.userRepository = userRepository;
        this.mlServiceClient = mlServiceClient;
    }

    @Transactional
    public RecommendationResponseDTO createRecommendation(Long userId, RecommendationRequestDTO dto) {
        User user = userRepository.findById(userId).orElseThrow();
        FoodCommodity food = foodRepository.findById(dto.getFoodCommodityId()).orElseThrow();
        
        RecommendationRequest request = RecommendationRequest.builder()
            .user(user).foodCommodity(food)
            .status("PENDING")
            .storageTemperature(dto.getStorageTemperature())
            .storageHumidity(dto.getStorageHumidity())
            .requiredShelfLifeDays(dto.getRequiredShelfLifeDays())
            .isRefrigerated(dto.getIsRefrigerated())
            .lightExposure(dto.getLightExposure())
            .costPreference(dto.getCostPreference())
            .sustainabilityPreference(dto.getSustainabilityPreference())
            .performancePreference(dto.getPerformancePreference())
            .shelfLifePreference(dto.getShelfLifePreference())
            .build();
        request = requestRepository.save(request);

        List<PackagingMaterial> materials = materialRepository.findAll();
        List<Map<String, Object>> payloads = new ArrayList<>();
        
        for (PackagingMaterial material : materials) {
            Map<String, Object> payload = new HashMap<>();
            payload.put("moisture_content", food.getFoodProperties().getMoistureContent());
            payload.put("ph_level", food.getFoodProperties().getPhLevel());
            payload.put("storage_temperature", dto.getStorageTemperature());
            if (material.getPackagingProperties() != null) {
                payload.put("oxygen_transmission_rate", material.getPackagingProperties().getOxygenTransmissionRate());
            }
            payloads.add(payload);
        }

        try {
            List<Map<String, Object>> predictions = mlServiceClient.batchPredict(payloads);
            
            List<RecommendationResult> results = new ArrayList<>();
            for (int i = 0; i < materials.size(); i++) {
                PackagingMaterial material = materials.get(i);
                Map<String, Object> pred = predictions.size() > i ? predictions.get(i) : new HashMap<>();
                Double suitabilityScore = (Double) pred.getOrDefault("suitability_score", 0.8);
                
                double costScore = 0.5;
                double sustainScore = 0.5;
                if (material.getPackagingProperties() != null) {
                    costScore = 1.0 - (material.getPackagingProperties().getEstimatedCostPerSqm() != null ? material.getPackagingProperties().getEstimatedCostPerSqm() / 100.0 : 0.5);
                    sustainScore = (material.getPackagingProperties().getSustainabilityScore() != null ? material.getPackagingProperties().getSustainabilityScore() : 5.0) / 10.0;
                }
                
                double finalScore = dto.getPerformancePreference() * suitabilityScore + 
                                  dto.getCostPreference() * costScore + 
                                  dto.getSustainabilityPreference() * sustainScore + 
                                  dto.getShelfLifePreference() * suitabilityScore;
                
                RecommendationResult result = RecommendationResult.builder()
                    .recommendationRequest(request)
                    .packagingMaterial(material)
                    .suitabilityScore(suitabilityScore)
                    .performanceScore(suitabilityScore)
                    .costScore(costScore)
                    .sustainabilityScore(sustainScore)
                    .overallScore(finalScore)
                    .build();
                results.add(result);
            }
            
            results.sort((a, b) -> Double.compare(b.getOverallScore(), a.getOverallScore()));
            List<RecommendationResult> topResults = results.stream().limit(3).collect(Collectors.toList());
            
            for (int i = 0; i < topResults.size(); i++) {
                RecommendationResult res = topResults.get(i);
                res.setRank(i + 1);
                res = resultRepository.save(res);
                RecommendationFactor factor = RecommendationFactor.builder()
                    .recommendationResult(res)
                    .factorName("Suitability")
                    .factorValue(res.getSuitabilityScore())
                    .contributionPercentage(50.0)
                    .direction("POSITIVE")
                    .build();
                factorRepository.save(factor);
            }
            
            request.setStatus("COMPLETED");
            requestRepository.save(request);
            
            RecommendationResponseDTO responseDTO = new RecommendationResponseDTO();
            responseDTO.setRequestId(request.getId());
            responseDTO.setStatus("COMPLETED");
            return responseDTO;
        } catch (Exception e) {
            request.setStatus("FAILED");
            requestRepository.save(request);
            throw new RuntimeException("Failed to get predictions", e);
        }
    }
}""",

    os.path.join(src_main_java, "service", "DashboardService.java"): """package com.foodpackaging.service;
import org.springframework.stereotype.Service;
@Service public class DashboardService { }""",

    os.path.join(src_main_java, "service", "SavedRecommendationService.java"): """package com.foodpackaging.service;
import org.springframework.stereotype.Service;
@Service public class SavedRecommendationService { }""",

    os.path.join(src_main_java, "service", "ModelService.java"): """package com.foodpackaging.service;
import org.springframework.stereotype.Service;
@Service public class ModelService { }"""
}

for path, content in files.items():
    write_file(path, content)
print("Services part 2 generated.")
