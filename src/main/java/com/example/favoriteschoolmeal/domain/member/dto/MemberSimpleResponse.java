package com.example.favoriteschoolmeal.domain.member.dto;

import com.example.favoriteschoolmeal.domain.file.domain.FileEntity;
import com.example.favoriteschoolmeal.domain.member.domain.Member;

import java.util.Optional;

public record MemberSimpleResponse(
        String nickname,
        String username,
        String profileImageEndpoint) {

    public static MemberSimpleResponse from(final Member member) {

        return new MemberSimpleResponse(
                member.getNickname(),
                member.getUsername(),
                Optional.ofNullable(member.getProfileImage())
                        .map(FileEntity::getEndpoint)
                        .orElse(null));
    }
}