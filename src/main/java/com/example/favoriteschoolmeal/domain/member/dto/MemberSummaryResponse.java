package com.example.favoriteschoolmeal.domain.member.dto;

import com.example.favoriteschoolmeal.domain.member.domain.Member;
import com.example.favoriteschoolmeal.domain.model.Authority;
import com.example.favoriteschoolmeal.domain.model.Gender;

import java.time.LocalDateTime;

public record MemberSummaryResponse(

        Long id,
        String username,
        String nickname,
        String fullname,
        String email,
        Authority authority,
        LocalDateTime unblockDate,
        Integer age,
        Gender gender,
        String introduction) {

    public static MemberSummaryResponse from(final Member member){
        return new MemberSummaryResponse(
                member.getId(),
                member.getUsername(),
                member.getNickname(),
                member.getFullName(),
                member.getEmail(),
                member.getAuthority(),
                member.getUnblockDate(),
                member.getAge(),
                member.getGender(),
                member.getIntroduction()
        );
    }
}
