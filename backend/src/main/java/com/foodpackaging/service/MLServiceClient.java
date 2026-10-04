package com.foodpackaging.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MLServiceClient {
    private final RestTemplate restTemplate;

    @Value("${app.ml-service.url}")
    private String mlServiceUrl;

    public MLServiceClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> predict(Map<String, Object> payload) {
        try {
            return restTemplate.postForObject(mlServiceUrl + "/predict", payload, Map.class);
        } catch (Exception e) {
            throw new RuntimeException("ML service prediction failed: " + e.getMessage(), e);
        }
    }

    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> batchPredict(List<Map<String, Object>> payloads) {
        try {
            Map<String, Object> request = new HashMap<>();
            request.put("predictions", payloads);
            Map<String, Object> response = restTemplate.postForObject(mlServiceUrl + "/batch-predict", request, Map.class);
            if (response != null && response.containsKey("results")) {
                return (List<Map<String, Object>>) response.get("results");
            }
            return Collections.emptyList();
        } catch (Exception e) {
            throw new RuntimeException("ML service batch prediction failed: " + e.getMessage(), e);
        }
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> explain(Map<String, Object> payload) {
        try {
            return restTemplate.postForObject(mlServiceUrl + "/explain", payload, Map.class);
        } catch (Exception e) {
            return Collections.emptyMap();
        }
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> getModelInfo() {
        try {
            return restTemplate.getForObject(mlServiceUrl + "/model-info", Map.class);
        } catch (Exception e) {
            Map<String, Object> fallback = new HashMap<>();
            fallback.put("model_name", "XGBoost Packaging Suitability Predictor");
            fallback.put("algorithm", "XGBoost Regressor");
            fallback.put("version", "1.0.0");
            fallback.put("status", "offline");
            return fallback;
        }
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> getHealth() {
        try {
            return restTemplate.getForObject(mlServiceUrl + "/health", Map.class);
        } catch (Exception e) {
            return Map.of("status", "offline", "model_loaded", false);
        }
    }
}
