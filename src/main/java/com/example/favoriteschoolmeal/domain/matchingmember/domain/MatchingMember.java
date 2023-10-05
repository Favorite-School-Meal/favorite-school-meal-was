package com.example.favoriteschoolmeal.domain.matchingmember.domain;

import com.example.favoriteschoolmeal.domain.matching.domain.Matching;
import com.example.favoriteschoolmeal.domain.member.domain.Member;
import com.example.favoriteschoolmeal.domain.model.RoleType;
import com.example.favoriteschoolmeal.global.common.Base;
import jakarta.persistence.*;
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
    @Column(name = "group_role", nullable = false)
    private RoleType roleType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "matching_id")
    private Matching matching;

    @Column(name = "is_accepted", nullable = false)
    private Boolean isAccepted;

    @Builder
    public MatchingMember(Member member, RoleType roleType, Matching matching, Boolean isAccepted) {
        this.member = member;
        this.roleType = roleType;
        this.matching = matching;
        this.isAccepted = isAccepted;
    }
}
