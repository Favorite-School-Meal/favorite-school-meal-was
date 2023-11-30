package com.example.favoriteschoolmeal.domain.member.dto;

import com.example.favoriteschoolmeal.domain.file.domain.FileEntity;
import com.example.favoriteschoolmeal.domain.member.domain.Member;
import java.util.Optional;

public record MemberSimpleResponse(

        Long memberId,
        String nickname,
        String username,
        String profileImageEndpoint) {

    public static MemberSimpleResponse from(final Member member) {

        return new MemberSimpleResponse(
                member.getId(),
                member.getNickname(),
                member.getUsername(),
                member.getProfileImageEndpoint());
    }
}