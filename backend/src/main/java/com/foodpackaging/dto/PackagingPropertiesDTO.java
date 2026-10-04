package com.foodpackaging.dto;
import lombok.Data;
@Data public class PackagingPropertiesDTO {
    private Double oxygenTransmissionRate;
    private Double waterVaporTransmissionRate;
    private Double lightTransmission;
    private Double thicknessMicrons;
    private Double tensileStrength;
    private String flexibility;
    private Boolean heatSealability;
    private Double transparency;
    private Double recyclabilityScore;
    private Double biodegradabilityScore;
    private Double estimatedCostPerSqm;
    private Double sustainabilityScore;
    private Double maxTemperature;
    private Double minTemperature;
    private Double greaseResistance;
}
