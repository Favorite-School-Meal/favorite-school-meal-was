package com.example.favoriteschoolmeal.domain.matching.domain;

import com.example.favoriteschoolmeal.domain.member.domain.Member;
import com.example.favoriteschoolmeal.domain.model.MatchingRequestStatus;
import com.example.favoriteschoolmeal.domain.model.RoleType;
import com.example.favoriteschoolmeal.global.common.Base;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "matching_member")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)

public class MatchingMember extends Base {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "matching_member_id", updatable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Enumerated(EnumType.STRING)
    @Column(name = "matching_role", nullable = false)
    private RoleType roleType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "matching_id")
    private Matching matching;

    @Enumerated(EnumType.STRING)
    @Column(name = "matching_request_status", nullable = false)
    private MatchingRequestStatus matchingRequestStatus;

    @Builder
    public MatchingMember(Member member, RoleType roleType, Matching matching,
            MatchingRequestStatus matchingRequestStatus) {
        this.member = member;
        this.roleType = roleType;
        this.matching = matching;
        this.matchingRequestStatus = matchingRequestStatus;
    }

    public void updateMatchingRequestStatus(MatchingRequestStatus status) {
        this.matchingRequestStatus = status;
    }
}
