package com.foodpackaging.service;

import com.foodpackaging.dto.*;
import com.foodpackaging.entity.*;
import com.foodpackaging.exception.BadRequestException;
import com.foodpackaging.exception.ResourceNotFoundException;
import com.foodpackaging.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class RecommendationService {

    private final RecommendationRequestRepository requestRepository;
    private final RecommendationResultRepository resultRepository;
    private final RecommendationFactorRepository factorRepository;
    private final PackagingMaterialRepository materialRepository;
    private final PackagingPropertiesRepository packagingPropertiesRepository;
    private final FoodCommodityRepository foodRepository;
    private final FoodPropertiesRepository foodPropertiesRepository;
    private final UserRepository userRepository;
    private final SavedRecommendationRepository savedRecommendationRepository;
    private final MLServiceClient mlServiceClient;

    public RecommendationService(
            RecommendationRequestRepository requestRepository,
            RecommendationResultRepository resultRepository,
            RecommendationFactorRepository factorRepository,
            PackagingMaterialRepository materialRepository,
            PackagingPropertiesRepository packagingPropertiesRepository,
            FoodCommodityRepository foodRepository,
            FoodPropertiesRepository foodPropertiesRepository,
            UserRepository userRepository,
            SavedRecommendationRepository savedRecommendationRepository,
            MLServiceClient mlServiceClient) {
        this.requestRepository = requestRepository;
        this.resultRepository = resultRepository;
        this.factorRepository = factorRepository;
        this.materialRepository = materialRepository;
        this.packagingPropertiesRepository = packagingPropertiesRepository;
        this.foodRepository = foodRepository;
        this.foodPropertiesRepository = foodPropertiesRepository;
        this.userRepository = userRepository;
        this.savedRecommendationRepository = savedRecommendationRepository;
        this.mlServiceClient = mlServiceClient;
    }

    @Transactional
    public RecommendationResponseDTO createRecommendation(Long userId, RecommendationRequestDTO dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        FoodCommodity food = foodRepository.findById(dto.getFoodCommodityId())
                .orElseThrow(() -> new ResourceNotFoundException("Food commodity not found"));

        // Build and save the recommendation request
        RecommendationRequest request = new RecommendationRequest();
        request.setUser(user);
        request.setFoodCommodity(food);
        request.setMoistureContent(dto.getMoistureContent());
        request.setPhLevel(dto.getPhLevel());
        request.setFatContent(dto.getFatContent());
        request.setProteinContent(dto.getProteinContent());
        request.setCarbohydrateContent(dto.getCarbohydrateContent());
        request.setRespirationRate(dto.getRespirationRate());
        request.setOxygenSensitivity(dto.getOxygenSensitivity());
        request.setMoistureSensitivity(dto.getMoistureSensitivity());
        request.setLightSensitivity(dto.getLightSensitivity());
        request.setStorageTemperature(dto.getStorageTemperature());
        request.setStorageHumidity(dto.getStorageHumidity());
        request.setRequiredShelfLifeDays(dto.getRequiredShelfLifeDays());
        request.setIsRefrigerated(dto.getIsRefrigerated());
        request.setLightExposure(dto.getLightExposure());
        request.setCostPreference(dto.getCostPreference());
        request.setSustainabilityPreference(dto.getSustainabilityPreference());
        request.setPerformancePreference(dto.getPerformancePreference());
        request.setShelfLifePreference(dto.getShelfLifePreference());
        request.setStatus("PENDING");
        request.setCreatedAt(LocalDateTime.now());
        request = requestRepository.save(request);

        // Load all packaging materials with properties
        List<PackagingMaterial> materials = materialRepository.findAll();
        if (materials.isEmpty()) {
            throw new BadRequestException("No packaging materials found in the database");
        }

        // Build prediction payloads for each material
        List<Map<String, Object>> payloads = new ArrayList<>();
        for (PackagingMaterial material : materials) {
            Map<String, Object> payload = buildPredictionPayload(dto, material);
            payloads.add(payload);
        }

        try {
            request.setStatus("PROCESSING");
            requestRepository.save(request);

            // Call ML service for batch predictions
            Map<String, Object> batchRequest = new HashMap<>();
            batchRequest.put("predictions", payloads);
            List<Map<String, Object>> predictions = mlServiceClient.batchPredict(payloads);

            // Calculate scores and rank materials
            List<ScoredMaterial> scoredMaterials = new ArrayList<>();
            double maxCost = materials.stream()
                    .filter(m -> m.getPackagingProperties() != null)
                    .mapToDouble(m -> m.getPackagingProperties().getEstimatedCostPerSqm() != null
                            ? m.getPackagingProperties().getEstimatedCostPerSqm() : 0)
                    .max().orElse(10.0);

            for (int i = 0; i < materials.size(); i++) {
                PackagingMaterial material = materials.get(i);
                Map<String, Object> pred = (i < predictions.size()) ? predictions.get(i) : new HashMap<>();

                double suitabilityScore = extractDouble(pred, "suitability_score", 0.5);
                suitabilityScore = Math.max(0.0, Math.min(1.0, suitabilityScore));

                // Calculate cost score (inverse normalized cost: cheaper = higher score)
                double costScore = 0.5;
                double sustainScore = 0.5;
                if (material.getPackagingProperties() != null) {
                    double cost = material.getPackagingProperties().getEstimatedCostPerSqm() != null
                            ? material.getPackagingProperties().getEstimatedCostPerSqm() : 0;
                    costScore = 1.0 - (cost / (maxCost + 0.01));

                    double sustain = material.getPackagingProperties().getSustainabilityScore() != null
                            ? material.getPackagingProperties().getSustainabilityScore() : 5.0;
                    sustainScore = sustain / 10.0;
                }

                // Apply user preference weights
                double perfWeight = dto.getPerformancePreference() != null ? dto.getPerformancePreference() : 0.4;
                double costWeight = dto.getCostPreference() != null ? dto.getCostPreference() : 0.2;
                double sustainWeight = dto.getSustainabilityPreference() != null ? dto.getSustainabilityPreference() : 0.2;
                double shelfWeight = dto.getShelfLifePreference() != null ? dto.getShelfLifePreference() : 0.2;

                // Normalize weights
                double totalWeight = perfWeight + costWeight + sustainWeight + shelfWeight;
                if (totalWeight > 0) {
                    perfWeight /= totalWeight;
                    costWeight /= totalWeight;
                    sustainWeight /= totalWeight;
                    shelfWeight /= totalWeight;
                }

                double finalScore = perfWeight * suitabilityScore
                        + costWeight * costScore
                        + sustainWeight * sustainScore
                        + shelfWeight * suitabilityScore;

                // Extract SHAP values from prediction
                @SuppressWarnings("unchecked")
                Map<String, Double> shapValues = pred.containsKey("shap_values")
                        ? (Map<String, Double>) pred.get("shap_values")
                        : new HashMap<>();

                scoredMaterials.add(new ScoredMaterial(material, suitabilityScore,
                        costScore, sustainScore, finalScore, shapValues, pred));
            }

            // Sort by overall score descending
            scoredMaterials.sort((a, b) -> Double.compare(b.overallScore, a.overallScore));

            // Take top 3 and get detailed SHAP explanations
            List<ScoredMaterial> top3 = scoredMaterials.stream().limit(3).collect(Collectors.toList());

            // For top 3, get detailed SHAP explanations if not already provided
            for (int i = 0; i < top3.size(); i++) {
                ScoredMaterial sm = top3.get(i);
                if (sm.shapValues.isEmpty()) {
                    try {
                        Map<String, Object> payload = buildPredictionPayload(dto, sm.material);
                        Map<String, Object> explanation = mlServiceClient.explain(payload);
                        if (explanation != null && explanation.containsKey("shap_values")) {
                            @SuppressWarnings("unchecked")
                            Map<String, Double> shap = (Map<String, Double>) explanation.get("shap_values");
                            sm.shapValues = shap;
                        }
                    } catch (Exception ignored) {
                        // SHAP explanation is optional, continue without it
                    }
                }
            }

            // Save results to database
            List<RecommendationResultDTO> resultDTOs = new ArrayList<>();
            for (int i = 0; i < top3.size(); i++) {
                ScoredMaterial sm = top3.get(i);
                RecommendationResult result = new RecommendationResult();
                result.setRecommendationRequest(request);
                result.setPackagingMaterial(sm.material);
                result.setRank(i + 1);
                result.setSuitabilityScore(sm.suitabilityScore);
                result.setPerformanceScore(sm.suitabilityScore);
                result.setCostScore(sm.costScore);
                result.setSustainabilityScore(sm.sustainScore);
                result.setOverallScore(sm.overallScore);
                result.setExplanation(generateExplanation(sm));
                result.setCreatedAt(LocalDateTime.now());
                result = resultRepository.save(result);

                // Save SHAP factors
                List<RecommendationFactorDTO> factorDTOs = new ArrayList<>();
                if (sm.shapValues != null && !sm.shapValues.isEmpty()) {
                    List<Map.Entry<String, Double>> sortedFactors = sm.shapValues.entrySet().stream()
                            .sorted((a, b) -> Double.compare(Math.abs(extractDoubleFromObj(b.getValue())),
                                    Math.abs(extractDoubleFromObj(a.getValue()))))
                            .limit(10)
                            .collect(Collectors.toList());

                    for (Map.Entry<String, Double> entry : sortedFactors) {
                        double value = extractDoubleFromObj(entry.getValue());
                        RecommendationFactor factor = new RecommendationFactor();
                        factor.setRecommendationResult(result);
                        factor.setFactorName(formatFeatureName(entry.getKey()));
                        factor.setFactorValue(value);
                        factor.setContributionPercentage(Math.abs(value) * 100);
                        factor.setDirection(value >= 0 ? "POSITIVE" : "NEGATIVE");
                        factor.setCreatedAt(LocalDateTime.now());
                        factorRepository.save(factor);

                        RecommendationFactorDTO factorDTO = new RecommendationFactorDTO();
                        factorDTO.setFactorName(formatFeatureName(entry.getKey()));
                        factorDTO.setFactorValue(value);
                        factorDTO.setContributionPercentage(Math.abs(value) * 100);
                        factorDTO.setDirection(value >= 0 ? "POSITIVE" : "NEGATIVE");
                        factorDTOs.add(factorDTO);
                    }
                }

                // Build result DTO
                RecommendationResultDTO resultDTO = new RecommendationResultDTO();
                resultDTO.setMaterialId(sm.material.getId());
                resultDTO.setMaterialName(sm.material.getName());
                resultDTO.setMaterialCode(sm.material.getMaterialCode());
                resultDTO.setRank(i + 1);
                resultDTO.setSuitabilityScore(Math.round(sm.suitabilityScore * 10000.0) / 100.0);
                resultDTO.setPerformanceScore(Math.round(sm.suitabilityScore * 10000.0) / 100.0);
                resultDTO.setCostScore(Math.round(sm.costScore * 10000.0) / 100.0);
                resultDTO.setSustainabilityScore(Math.round(sm.sustainScore * 10000.0) / 100.0);
                resultDTO.setOverallScore(Math.round(sm.overallScore * 10000.0) / 100.0);
                resultDTO.setExplanation(result.getExplanation());
                resultDTO.setFactors(factorDTOs);

                if (sm.material.getPackagingProperties() != null) {
                    PackagingPropertiesDTO propDTO = mapPackagingProperties(sm.material.getPackagingProperties());
                    resultDTO.setMaterialProperties(propDTO);
                }

                resultDTOs.add(resultDTO);
            }

            // Update request status to COMPLETED
            request.setStatus("COMPLETED");
            request.setModelVersion("XGBoost v1.0");
            requestRepository.save(request);

            // Build response
            RecommendationResponseDTO response = new RecommendationResponseDTO();
            response.setRequestId(request.getId());
            response.setFoodCommodityName(food.getName());
            response.setStatus("COMPLETED");
            response.setResults(resultDTOs);
            response.setCreatedAt(request.getCreatedAt());

            return response;

        } catch (Exception e) {
            request.setStatus("FAILED");
            requestRepository.save(request);
            throw new RuntimeException("Failed to generate recommendation: " + e.getMessage(), e);
        }
    }

    public RecommendationResponseDTO getRecommendation(Long id) {
        RecommendationRequest request = requestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Recommendation not found"));

        RecommendationResponseDTO response = new RecommendationResponseDTO();
        response.setRequestId(request.getId());
        response.setFoodCommodityName(request.getFoodCommodity() != null ? request.getFoodCommodity().getName() : "Unknown");
        response.setStatus(request.getStatus());
        response.setCreatedAt(request.getCreatedAt());

        List<RecommendationResult> results = resultRepository.findByRecommendationRequestIdOrderByRankAsc(request.getId());
        List<RecommendationResultDTO> resultDTOs = results.stream().map(r -> {
            RecommendationResultDTO dto = new RecommendationResultDTO();
            dto.setMaterialId(r.getPackagingMaterial().getId());
            dto.setMaterialName(r.getPackagingMaterial().getName());
            dto.setMaterialCode(r.getPackagingMaterial().getMaterialCode());
            dto.setRank(r.getRank());
            dto.setSuitabilityScore(Math.round(r.getSuitabilityScore() * 10000.0) / 100.0);
            dto.setPerformanceScore(Math.round(r.getPerformanceScore() * 10000.0) / 100.0);
            dto.setCostScore(Math.round(r.getCostScore() * 10000.0) / 100.0);
            dto.setSustainabilityScore(Math.round(r.getSustainabilityScore() * 10000.0) / 100.0);
            dto.setOverallScore(Math.round(r.getOverallScore() * 10000.0) / 100.0);
            dto.setExplanation(r.getExplanation());

            // Load SHAP factors
            List<RecommendationFactor> factors = factorRepository.findByRecommendationResultId(r.getId());
            dto.setFactors(factors.stream().map(f -> {
                RecommendationFactorDTO fDTO = new RecommendationFactorDTO();
                fDTO.setFactorName(f.getFactorName());
                fDTO.setFactorValue(f.getFactorValue());
                fDTO.setContributionPercentage(f.getContributionPercentage());
                fDTO.setDirection(f.getDirection());
                return fDTO;
            }).collect(Collectors.toList()));

            if (r.getPackagingMaterial().getPackagingProperties() != null) {
                dto.setMaterialProperties(mapPackagingProperties(r.getPackagingMaterial().getPackagingProperties()));
            }
            return dto;
        }).collect(Collectors.toList());

        response.setResults(resultDTOs);
        return response;
    }

    public List<RecommendationHistoryDTO> getUserHistory(Long userId) {
        List<RecommendationRequest> requests = requestRepository.findByUserIdOrderByCreatedAtDesc(userId);
        return requests.stream().map(r -> {
            RecommendationHistoryDTO dto = new RecommendationHistoryDTO();
            dto.setId(r.getId());
            dto.setFoodCommodityName(r.getFoodCommodity() != null ? r.getFoodCommodity().getName() : "Unknown");
            dto.setStatus(r.getStatus());
            dto.setCreatedAt(r.getCreatedAt());

            // Get top material
            List<RecommendationResult> results = resultRepository.findByRecommendationRequestIdOrderByRankAsc(r.getId());
            if (!results.isEmpty()) {
                RecommendationResult top = results.get(0);
                dto.setTopMaterial(top.getPackagingMaterial().getName());
                dto.setTopScore(Math.round(top.getOverallScore() * 10000.0) / 100.0);
            }
            return dto;
        }).collect(Collectors.toList());
    }

    public List<RecommendationResponseDTO> getUserRecommendations(Long userId) {
        List<RecommendationRequest> requests = requestRepository.findByUserIdOrderByCreatedAtDesc(userId);
        return requests.stream().map(r -> {
            RecommendationResponseDTO dto = new RecommendationResponseDTO();
            dto.setRequestId(r.getId());
            dto.setFoodCommodityName(r.getFoodCommodity() != null ? r.getFoodCommodity().getName() : "Unknown");
            dto.setStatus(r.getStatus());
            dto.setCreatedAt(r.getCreatedAt());
            return dto;
        }).collect(Collectors.toList());
    }

    // --- Helper methods ---

    private Map<String, Object> buildPredictionPayload(RecommendationRequestDTO dto, PackagingMaterial material) {
        Map<String, Object> payload = new LinkedHashMap<>();

        // Food features
        payload.put("moisture_content", dto.getMoistureContent() != null ? dto.getMoistureContent() : 0);
        payload.put("ph_level", dto.getPhLevel() != null ? dto.getPhLevel() : 7.0);
        payload.put("fat_content", dto.getFatContent() != null ? dto.getFatContent() : 0);
        payload.put("protein_content", dto.getProteinContent() != null ? dto.getProteinContent() : 0);
        payload.put("carbohydrate_content", dto.getCarbohydrateContent() != null ? dto.getCarbohydrateContent() : 0);
        payload.put("respiration_rate", dto.getRespirationRate() != null ? dto.getRespirationRate() : 0);
        payload.put("oxygen_sensitivity", encodeSensitivity(dto.getOxygenSensitivity()));
        payload.put("moisture_sensitivity", encodeSensitivity(dto.getMoistureSensitivity()));
        payload.put("light_sensitivity", encodeSensitivity(dto.getLightSensitivity()));
        payload.put("microbial_sensitivity", 1); // Default medium
        payload.put("is_perishable", dto.getMoistureContent() != null && dto.getMoistureContent() > 50 ? 1 : 0);

        // Storage features
        payload.put("storage_temperature", dto.getStorageTemperature() != null ? dto.getStorageTemperature() : 25.0);
        payload.put("storage_humidity", dto.getStorageHumidity() != null ? dto.getStorageHumidity() : 60.0);
        payload.put("required_shelf_life_days", dto.getRequiredShelfLifeDays() != null ? dto.getRequiredShelfLifeDays() : 30);
        payload.put("is_refrigerated", dto.getIsRefrigerated() != null && dto.getIsRefrigerated() ? 1 : 0);
        payload.put("light_exposure", encodeLightExposure(dto.getLightExposure()));

        // Packaging features
        PackagingProperties pp = material.getPackagingProperties();
        if (pp != null) {
            payload.put("oxygen_transmission_rate", pp.getOxygenTransmissionRate() != null ? pp.getOxygenTransmissionRate() : 1000);
            payload.put("water_vapor_transmission_rate", pp.getWaterVaporTransmissionRate() != null ? pp.getWaterVaporTransmissionRate() : 10);
            payload.put("light_transmission", pp.getLightTransmission() != null ? pp.getLightTransmission() : 50);
            payload.put("thickness_microns", pp.getThicknessMicrons() != null ? pp.getThicknessMicrons() : 50);
            payload.put("tensile_strength", pp.getTensileStrength() != null ? pp.getTensileStrength() : 20);
            payload.put("flexibility", encodeFlexibility(pp.getFlexibility()));
            payload.put("heat_sealability", pp.getHeatSealability() != null && pp.getHeatSealability() ? 1 : 0);
            payload.put("recyclability_score", pp.getRecyclabilityScore() != null ? pp.getRecyclabilityScore() : 5);
            payload.put("biodegradability_score", pp.getBiodegradabilityScore() != null ? pp.getBiodegradabilityScore() : 1);
            payload.put("estimated_cost_per_sqm", pp.getEstimatedCostPerSqm() != null ? pp.getEstimatedCostPerSqm() : 1.0);
        } else {
            payload.put("oxygen_transmission_rate", 1000);
            payload.put("water_vapor_transmission_rate", 10);
            payload.put("light_transmission", 50);
            payload.put("thickness_microns", 50);
            payload.put("tensile_strength", 20);
            payload.put("flexibility", 1);
            payload.put("heat_sealability", 1);
            payload.put("recyclability_score", 5);
            payload.put("biodegradability_score", 1);
            payload.put("estimated_cost_per_sqm", 1.0);
        }

        return payload;
    }

    private int encodeSensitivity(String sensitivity) {
        if (sensitivity == null) return 1;
        switch (sensitivity.toLowerCase()) {
            case "low": return 0;
            case "medium": return 1;
            case "high": return 2;
            case "very high": return 3;
            default: return 1;
        }
    }

    private int encodeLightExposure(String exposure) {
        if (exposure == null) return 1;
        switch (exposure.toLowerCase()) {
            case "none": return 0;
            case "low": return 1;
            case "high": return 2;
            default: return 1;
        }
    }

    private int encodeFlexibility(String flexibility) {
        if (flexibility == null) return 1;
        switch (flexibility.toLowerCase()) {
            case "low": return 0;
            case "medium": return 1;
            case "high": return 2;
            default: return 1;
        }
    }

    private double extractDouble(Map<String, Object> map, String key, double defaultVal) {
        Object val = map.get(key);
        if (val instanceof Number) return ((Number) val).doubleValue();
        if (val instanceof String) {
            try { return Double.parseDouble((String) val); } catch (NumberFormatException e) { return defaultVal; }
        }
        return defaultVal;
    }

    private double extractDoubleFromObj(Object val) {
        if (val instanceof Number) return ((Number) val).doubleValue();
        return 0.0;
    }

    private String generateExplanation(ScoredMaterial sm) {
        StringBuilder sb = new StringBuilder();
        sb.append(sm.material.getName()).append(" achieved a suitability score of ")
                .append(String.format("%.1f%%", sm.suitabilityScore * 100)).append(". ");

        if (sm.material.getPackagingProperties() != null) {
            PackagingProperties pp = sm.material.getPackagingProperties();
            if (pp.getOxygenTransmissionRate() != null && pp.getOxygenTransmissionRate() < 100) {
                sb.append("Excellent oxygen barrier. ");
            }
            if (pp.getWaterVaporTransmissionRate() != null && pp.getWaterVaporTransmissionRate() < 5) {
                sb.append("Strong moisture barrier. ");
            }
            if (pp.getLightTransmission() != null && pp.getLightTransmission() < 10) {
                sb.append("Excellent light protection. ");
            }
            if (pp.getSustainabilityScore() != null && pp.getSustainabilityScore() >= 7) {
                sb.append("Highly sustainable material. ");
            }
            if (pp.getRecyclabilityScore() != null && pp.getRecyclabilityScore() >= 7) {
                sb.append("Highly recyclable. ");
            }
        }

        return sb.toString().trim();
    }

    private String formatFeatureName(String feature) {
        if (feature == null) return "Unknown";
        return feature.replace("_", " ")
                .substring(0, 1).toUpperCase() + feature.replace("_", " ").substring(1);
    }

    private PackagingPropertiesDTO mapPackagingProperties(PackagingProperties pp) {
        PackagingPropertiesDTO dto = new PackagingPropertiesDTO();
        dto.setOxygenTransmissionRate(pp.getOxygenTransmissionRate());
        dto.setWaterVaporTransmissionRate(pp.getWaterVaporTransmissionRate());
        dto.setLightTransmission(pp.getLightTransmission());
        dto.setThicknessMicrons(pp.getThicknessMicrons());
        dto.setTensileStrength(pp.getTensileStrength());
        dto.setFlexibility(pp.getFlexibility());
        dto.setHeatSealability(pp.getHeatSealability());
        dto.setRecyclabilityScore(pp.getRecyclabilityScore());
        dto.setBiodegradabilityScore(pp.getBiodegradabilityScore());
        dto.setEstimatedCostPerSqm(pp.getEstimatedCostPerSqm());
        dto.setSustainabilityScore(pp.getSustainabilityScore());
        dto.setMaxTemperature(pp.getMaxTemperature());
        dto.setMinTemperature(pp.getMinTemperature());
        return dto;
    }

    // Inner helper class
    private static class ScoredMaterial {
        PackagingMaterial material;
        double suitabilityScore;
        double costScore;
        double sustainScore;
        double overallScore;
        Map<String, Double> shapValues;
        Map<String, Object> prediction;

        ScoredMaterial(PackagingMaterial material, double suitabilityScore, double costScore,
                       double sustainScore, double overallScore,
                       Map<String, Double> shapValues, Map<String, Object> prediction) {
            this.material = material;
            this.suitabilityScore = suitabilityScore;
            this.costScore = costScore;
            this.sustainScore = sustainScore;
            this.overallScore = overallScore;
            this.shapValues = shapValues;
            this.prediction = prediction;
        }
    }
}
