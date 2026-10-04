package com.foodpackaging.service;

import com.foodpackaging.dto.CreatePackagingMaterialRequest;
import com.foodpackaging.dto.PackagingMaterialDTO;
import com.foodpackaging.dto.PackagingPropertiesDTO;
import com.foodpackaging.entity.PackagingMaterial;
import com.foodpackaging.entity.PackagingProperties;
import com.foodpackaging.exception.ResourceNotFoundException;
import com.foodpackaging.repository.PackagingMaterialRepository;
import com.foodpackaging.repository.PackagingPropertiesRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PackagingMaterialService {

    private final PackagingMaterialRepository repository;
    private final PackagingPropertiesRepository propertiesRepository;

    public PackagingMaterialService(PackagingMaterialRepository repository, PackagingPropertiesRepository propertiesRepository) {
        this.repository = repository;
        this.propertiesRepository = propertiesRepository;
    }

    public List<PackagingMaterialDTO> getAll(String category) {
        List<PackagingMaterial> list;
        if (category != null && !category.trim().isEmpty()) {
            list = repository.findByCategory(category.trim());
        } else {
            list = repository.findAll();
        }
        return list.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public PackagingMaterialDTO getById(Long id) {
        PackagingMaterial material = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Packaging material not found with id: " + id));
        return mapToDTO(material);
    }

    @Transactional
    public PackagingMaterialDTO create(CreatePackagingMaterialRequest request) {
        PackagingMaterial material = PackagingMaterial.builder()
                .name(request.getName())
                .materialCode(request.getMaterialCode())
                .category(request.getCategory())
                .description(request.getDescription())
                .build();
        material = repository.save(material);

        if (request.getProperties() != null) {
            PackagingProperties props = mapToEntity(request.getProperties(), material);
            props = propertiesRepository.save(props);
            material.setPackagingProperties(props);
        }

        return mapToDTO(material);
    }

    @Transactional
    public PackagingMaterialDTO update(Long id, CreatePackagingMaterialRequest request) {
        PackagingMaterial material = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Packaging material not found with id: " + id));

        material.setName(request.getName());
        material.setMaterialCode(request.getMaterialCode());
        material.setCategory(request.getCategory());
        material.setDescription(request.getDescription());

        if (request.getProperties() != null) {
            PackagingProperties props = material.getPackagingProperties();
            if (props == null) {
                props = new PackagingProperties();
                props.setPackagingMaterial(material);
            }
            updatePropertiesFromDTO(props, request.getProperties());
            props = propertiesRepository.save(props);
            material.setPackagingProperties(props);
        }

        material = repository.save(material);
        return mapToDTO(material);
    }

    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Packaging material not found with id: " + id);
        }
        repository.deleteById(id);
    }

    public PackagingMaterialDTO mapToDTO(PackagingMaterial material) {
        PackagingMaterialDTO dto = new PackagingMaterialDTO();
        dto.setId(material.getId());
        dto.setName(material.getName());
        dto.setMaterialCode(material.getMaterialCode());
        dto.setCategory(material.getCategory());
        dto.setDescription(material.getDescription());

        if (material.getPackagingProperties() != null) {
            PackagingProperties p = material.getPackagingProperties();
            PackagingPropertiesDTO pdto = new PackagingPropertiesDTO();
            pdto.setOxygenTransmissionRate(p.getOxygenTransmissionRate());
            pdto.setWaterVaporTransmissionRate(p.getWaterVaporTransmissionRate());
            pdto.setLightTransmission(p.getLightTransmission());
            pdto.setThicknessMicrons(p.getThicknessMicrons());
            pdto.setTensileStrength(p.getTensileStrength());
            pdto.setFlexibility(p.getFlexibility());
            pdto.setHeatSealability(p.getHeatSealability());
            pdto.setTransparency(p.getTransparency());
            pdto.setRecyclabilityScore(p.getRecyclabilityScore());
            pdto.setBiodegradabilityScore(p.getBiodegradabilityScore());
            pdto.setEstimatedCostPerSqm(p.getEstimatedCostPerSqm());
            pdto.setSustainabilityScore(p.getSustainabilityScore());
            pdto.setMaxTemperature(p.getMaxTemperature());
            pdto.setMinTemperature(p.getMinTemperature());
            pdto.setGreaseResistance(p.getGreaseResistance());
            dto.setProperties(pdto);
        }
        return dto;
    }

    private PackagingProperties mapToEntity(PackagingPropertiesDTO pdto, PackagingMaterial material) {
        PackagingProperties p = new PackagingProperties();
        p.setPackagingMaterial(material);
        updatePropertiesFromDTO(p, pdto);
        return p;
    }

    private void updatePropertiesFromDTO(PackagingProperties p, PackagingPropertiesDTO pdto) {
        p.setOxygenTransmissionRate(pdto.getOxygenTransmissionRate());
        p.setWaterVaporTransmissionRate(pdto.getWaterVaporTransmissionRate());
        p.setLightTransmission(pdto.getLightTransmission());
        p.setThicknessMicrons(pdto.getThicknessMicrons());
        p.setTensileStrength(pdto.getTensileStrength());
        p.setFlexibility(pdto.getFlexibility());
        p.setHeatSealability(pdto.getHeatSealability());
        p.setTransparency(pdto.getTransparency());
        p.setRecyclabilityScore(pdto.getRecyclabilityScore());
        p.setBiodegradabilityScore(pdto.getBiodegradabilityScore());
        p.setEstimatedCostPerSqm(pdto.getEstimatedCostPerSqm());
        p.setSustainabilityScore(pdto.getSustainabilityScore());
        p.setMaxTemperature(pdto.getMaxTemperature());
        p.setMinTemperature(pdto.getMinTemperature());
        p.setGreaseResistance(pdto.getGreaseResistance());
    }
}
