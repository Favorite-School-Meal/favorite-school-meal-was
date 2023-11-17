package com.example.favoriteschoolmeal.domain.matching.service;

import com.example.favoriteschoolmeal.domain.matching.domain.Matching;
import com.example.favoriteschoolmeal.domain.matching.repository.MatchingRepository;
import com.example.favoriteschoolmeal.domain.model.MatchingState;
import java.time.LocalDateTime;
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
    
    public Matching addMatching(final LocalDateTime meetingDateTime, final Integer maxParticipant) {
        final Matching matching = Matching.builder()
                .matchingState(MatchingState.IN_PROGRESS)
                .maxParticipant(maxParticipant)
                .meetingDateTime(meetingDateTime)
                .build();
        return matchingRepository.save(matching);
    }

    public void modifyDetails(final Matching matching, final LocalDateTime meetingDateTime, final Integer maxParticipant) {
        matching.modifyDetails(meetingDateTime, maxParticipant);
        matchingRepository.save(matching);
    }
}
