package com.example.favoriteschoolmeal.domain.oauth2.repository;

import com.example.favoriteschoolmeal.domain.member.domain.Member;
import com.example.favoriteschoolmeal.domain.model.OauthPlatform;
import com.example.favoriteschoolmeal.domain.oauth2.domain.Oauth;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OauthRepository extends JpaRepository<Oauth, Long> {


    Optional<Oauth> findByMember(Member member);

    Optional<Oauth> findByPlatformIdAndOauthPlatform(String platformId,
            OauthPlatform oauthPlatform);

}
