package com.example.favoriteschoolmeal.domain.oauth2.repository;

import com.example.favoriteschoolmeal.domain.oauth2.domain.Oauth;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OauthRepository extends JpaRepository<Oauth, Long> {


}
