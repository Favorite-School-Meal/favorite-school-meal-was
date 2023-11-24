package com.example.favoriteschoolmeal.domain.matching.domain;


import com.example.favoriteschoolmeal.domain.model.MatchingStatus;
import com.example.favoriteschoolmeal.global.common.util.DateTimeUtil;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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

    @Column(name = "start_date_time", nullable = false)
    private LocalDateTime startDateTime;

    @Column(name = "end_date_time", nullable = false)
    private LocalDateTime endDateTime;

    @Builder
    public Matching(MatchingStatus matchingStatus, Integer maxParticipant,
            LocalDateTime startDateTime, LocalDateTime endDateTime) {
        this.matchingStatus = matchingStatus;
        this.maxParticipant = maxParticipant;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
    }

    public void modifyDetails(LocalDateTime startDateTime, LocalDateTime endDateTime,
            Integer maxParticipant) {
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.maxParticipant = maxParticipant;
    }

    public void completeMatching() {
        this.matchingStatus = MatchingStatus.CLOSED;
    }

    public String getMeetingDateTime() {
        return DateTimeUtil.formatDateTimeRange(startDateTime, endDateTime);
    }
}