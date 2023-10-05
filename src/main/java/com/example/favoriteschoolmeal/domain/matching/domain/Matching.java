package com.example.favoriteschoolmeal.domain.matching.domain;


import com.example.favoriteschoolmeal.domain.model.GroupState;
import com.example.favoriteschoolmeal.domain.model.Location;
import jakarta.persistence.*;
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
    @Column(name = "group_state", nullable = false)
    private GroupState groupState;

    @Column(name = "max_participant", nullable = false)
    private Long maxParticipant;

    @Embedded
    private Location location;

    @Builder
    public Matching(GroupState groupState, Long maxParticipant, Location location) {
        this.groupState = groupState;
        this.maxParticipant = maxParticipant;
        this.location = location;
    }
}
