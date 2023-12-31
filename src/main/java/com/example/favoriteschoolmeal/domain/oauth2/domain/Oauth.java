package com.example.favoriteschoolmeal.domain.oauth2.domain;


import com.example.favoriteschoolmeal.domain.member.domain.Member;
import com.example.favoriteschoolmeal.domain.model.OauthPlatform;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "oauth")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Oauth {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
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


    @Builder
    public Oauth(OauthPlatform oauthPlatform, String platformId, String email, String nickname,
            Member member) {
        this.oauthPlatform = oauthPlatform;
        this.platformId = platformId;
        this.email = email;
        this.nickname = nickname;
        this.member = member;
    }

}
