package com.example.favoriteschoolmeal.domain.matching.domain;


import com.example.favoriteschoolmeal.domain.model.MatchingStatus;
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
    @Column(name = "matching_status", nullable = false)
    private MatchingStatus matchingStatus;

    @Column(name = "max_participant", nullable = false)
    private Integer maxParticipant;

    @Column(name = "meeting_date_time", nullable = false)
    private LocalDateTime meetingDateTime;

    @Builder
    public Matching(MatchingStatus matchingStatus, Integer maxParticipant, LocalDateTime meetingDateTime) {
        this.matchingStatus = matchingStatus;
        this.maxParticipant = maxParticipant;
        this.meetingDateTime = meetingDateTime;
    }

    public void modifyDetails(final LocalDateTime meetingDateTime, final Integer maxParticipant) {
        this.meetingDateTime = meetingDateTime;
        this.maxParticipant = maxParticipant;
    }
}
