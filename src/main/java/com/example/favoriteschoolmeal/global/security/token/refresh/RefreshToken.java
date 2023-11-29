package com.example.favoriteschoolmeal.global.security.token.refresh;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshToken {

    @Column(nullable = false, length = 60)
    String username;
    @Id
    @Column(name = "refresh_token_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String refreshToken;

    @Builder
    public RefreshToken(Long id, String refreshToken, String username) {
        this.id = id;
        this.refreshToken = refreshToken;
        this.username = username;
    }

}
