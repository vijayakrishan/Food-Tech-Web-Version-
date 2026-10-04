package com.foodpackaging.service;

import com.foodpackaging.dto.RecommendationResponseDTO;
import com.foodpackaging.entity.RecommendationRequest;
import com.foodpackaging.entity.SavedRecommendation;
import com.foodpackaging.entity.User;
import com.foodpackaging.exception.BadRequestException;
import com.foodpackaging.exception.ResourceNotFoundException;
import com.foodpackaging.repository.RecommendationRequestRepository;
import com.foodpackaging.repository.SavedRecommendationRepository;
import com.foodpackaging.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SavedRecommendationService {

    private final SavedRecommendationRepository savedRepository;
    private final UserRepository userRepository;
    private final RecommendationRequestRepository requestRepository;
    private final RecommendationService recommendationService;

    public SavedRecommendationService(
            SavedRecommendationRepository savedRepository,
            UserRepository userRepository,
            RecommendationRequestRepository requestRepository,
            RecommendationService recommendationService) {
        this.savedRepository = savedRepository;
        this.userRepository = userRepository;
        this.requestRepository = requestRepository;
        this.recommendationService = recommendationService;
    }

    @Transactional
    public void save(Long userId, Long requestId, String notes) {
        if (savedRepository.existsByUserIdAndRecommendationRequestId(userId, requestId)) {
            throw new BadRequestException("Recommendation is already saved");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        RecommendationRequest request = requestRepository.findById(requestId)
                .orElseThrow(() -> new ResourceNotFoundException("Recommendation request not found"));

        SavedRecommendation saved = SavedRecommendation.builder()
                .user(user)
                .recommendationRequest(request)
                .notes(notes)
                .build();

        savedRepository.save(saved);
    }

    @Transactional
    public void unsave(Long userId, Long requestId) {
        savedRepository.deleteByUserIdAndRecommendationRequestId(userId, requestId);
    }

    public boolean isSaved(Long userId, Long requestId) {
        return Boolean.TRUE.equals(savedRepository.existsByUserIdAndRecommendationRequestId(userId, requestId));
    }

    public List<RecommendationResponseDTO> getSavedByUser(Long userId) {
        List<SavedRecommendation> list = savedRepository.findByUserId(userId);
        return list.stream()
                .map(s -> recommendationService.getRecommendation(s.getRecommendationRequest().getId()))
                .collect(Collectors.toList());
    }
}
