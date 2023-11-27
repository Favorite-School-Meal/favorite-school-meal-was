package com.example.favoriteschoolmeal.domain.member.dto;

import com.example.favoriteschoolmeal.domain.file.domain.FileEntity;
import com.example.favoriteschoolmeal.domain.member.domain.Member;
import com.example.favoriteschoolmeal.domain.model.Gender;

import java.util.Optional;


public record MemberDetailResponse(

        String nickname,
        String fullname,
        String email,
        Integer age,
        Gender gender,
        String introduction,
        String profileImageUrl
) {

    public static MemberDetailResponse from(final Member member){
        return new MemberDetailResponse(

                member.getNickname(),
                member.getFullName(),
                member.getEmail(),
                member.getAge(),
                member.getGender(),
                member.getIntroduction(),
                Optional.ofNullable(member.getProfileImage())
                        .map(FileEntity::getUrl)
                        .orElse(null)
        );
    }
}
