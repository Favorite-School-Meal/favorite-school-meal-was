package com.example.favoriteschoolmeal.domain.oauth2.domain;


import com.example.favoriteschoolmeal.domain.member.domain.Member;
import com.example.favoriteschoolmeal.domain.model.OauthPlatform;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "oauth")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Oauth {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TINYINT", nullable = false)
    private OauthPlatform oauthPlatform;

    @Column(nullable = false)
    private String platformId;

    @Column
    private String email;

    @Column
    private String nickname;

    @JoinColumn(name = "member_id", nullable = false)
    @OneToOne(cascade = CascadeType.REMOVE)
    private Member member;

    @Column
    private LocalDateTime deletedAt;

    @Builder
    public Oauth(OauthPlatform oauthPlatform, String platformId, String email, String nickname, Member member) {
        this.oauthPlatform = oauthPlatform;
        this.platformId = platformId;
        this.email = email;
        this.nickname = nickname;
        this.member = member;
    }

    public boolean isDeleted() {
        return deletedAt != null;
    }
}
