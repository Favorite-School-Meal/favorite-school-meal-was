package com.example.favoriteschoolmeal.domain.matching.controller.dto;

import com.example.favoriteschoolmeal.domain.matching.domain.Matching;

public record MatchingResponse(
        Long matchingId,
        String matchingStatus,
        String meetingDateTime,
        Integer approvedParticipant,
        Integer maxParticipant) {

    public static MatchingResponse from(final Matching matching, final Integer approvedParticipant) {
        return new MatchingResponse(
                matching.getId(),
                matching.getMatchingStatus().name(),
                matching.getMeetingDateTime().toString(),
                approvedParticipant,
                matching.getMaxParticipant()
        );
    }
}
