package com.example.favoriteschoolmeal.domain.matching.service;

import com.example.favoriteschoolmeal.domain.matching.domain.Matching;
import com.example.favoriteschoolmeal.domain.matching.repository.MatchingRepository;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class MatchingService {

    private final MatchingRepository matchingRepository;

    public MatchingService(MatchingRepository matchingRepository) {
        this.matchingRepository = matchingRepository;
    }

    // TODO: 구현 필요
    public Optional<Matching> findMatchingOptionally(Long matchingId) {
        return matchingRepository.findById(matchingId);
    }

    // TODO: 구현 필요
    public Optional<Matching> addMatching() {
        return findMatchingOptionally(1L);
    }
}
