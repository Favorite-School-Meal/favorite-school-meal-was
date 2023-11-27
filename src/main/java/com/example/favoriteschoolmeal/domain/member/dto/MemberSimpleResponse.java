package com.example.favoriteschoolmeal.domain.member.dto;

import com.example.favoriteschoolmeal.domain.member.domain.Member;

public record MemberSimpleResponse(String nickname, String username) {

    public static MemberSimpleResponse from(final Member member) {

        return new MemberSimpleResponse(
                member.getNickname(),
                member.getUsername()
        );
    }
}
