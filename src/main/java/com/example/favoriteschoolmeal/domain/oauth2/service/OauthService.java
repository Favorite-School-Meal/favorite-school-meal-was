package com.example.favoriteschoolmeal.domain.oauth2.service;



import com.example.favoriteschoolmeal.domain.member.domain.Member;
import com.example.favoriteschoolmeal.domain.oauth2.domain.Oauth;
import com.example.favoriteschoolmeal.domain.oauth2.repository.OauthRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class OauthService {

    private final OauthRepository oauthRepository;

    public Optional<Oauth> findOauthOptionally(final Member member) {
        return oauthRepository.findByMember(member);
    }

    public void removeOauthByMember(final Member member){
        findOauthOptionally(member).ifPresent(oauthRepository::delete);
    }

}
