package com.example.favoriteschoolmeal.domain.member.dto;

import com.example.favoriteschoolmeal.domain.member.domain.Member;
import com.example.favoriteschoolmeal.domain.model.Gender;


public record MemberDetailResponse(

        String nickname,
        String fullname,
        String email,
        Integer age,
        Gender gender,
        String introduction) {

    public static MemberDetailResponse from(final Member member){
        return new MemberDetailResponse(

                member.getNickname(),
                member.getFullName(),
                member.getEmail(),
                member.getAge(),
                member.getGender(),
                member.getIntroduction(),

        );
    }
}
