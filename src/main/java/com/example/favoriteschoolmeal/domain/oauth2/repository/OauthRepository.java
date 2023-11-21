package com.example.favoriteschoolmeal.domain.oauth2.repository;

import com.example.favoriteschoolmeal.domain.model.OauthPlatform;
import com.example.favoriteschoolmeal.domain.oauth2.domain.Oauth;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OauthRepository extends JpaRepository<Oauth, Long> {

    Optional<Oauth> findByPlatformIdAndOauthPlatform(String platformId, OauthPlatform oauthPlatform);
}
