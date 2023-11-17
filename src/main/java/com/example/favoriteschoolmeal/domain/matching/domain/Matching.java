package com.example.favoriteschoolmeal.domain.matching.domain;


import com.example.favoriteschoolmeal.domain.model.MatchingState;
import com.example.favoriteschoolmeal.domain.model.Location;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "matching")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)

public class Matching {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "matching_id", updatable = false)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "matching_state", nullable = false)
    private MatchingState matchingState;

    @Column(name = "max_participant", nullable = false)
    private Long maxParticipant;

    @Column(name = "meeting_date_time", nullable = false)
    private LocalDateTime meetingDateTime;

    @Builder
    public Matching(MatchingState matchingState, Long maxParticipant, LocalDateTime meetingDateTime) {
        this.matchingState = matchingState;
        this.maxParticipant = maxParticipant;
        this.meetingDateTime = meetingDateTime;
    }
}
