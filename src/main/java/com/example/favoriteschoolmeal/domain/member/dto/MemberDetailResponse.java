package com.example.favoriteschoolmeal.domain.member.dto;

import com.example.favoriteschoolmeal.domain.file.domain.FileEntity;
import com.example.favoriteschoolmeal.domain.member.domain.Member;
import com.example.favoriteschoolmeal.domain.model.Gender;
import java.util.Optional;


public record MemberDetailResponse(

        Long memberId,
        String nickname,
        String fullname,
        String email,
        Integer age,
        Gender gender,
        String introduction,
        String profileImageEndpoint
) {

    public static MemberDetailResponse from(final Member member) {
        return new MemberDetailResponse(

                member.getId(),
                member.getNickname(),
                member.getFullName(),
                member.getEmail(),
                member.getAge(),
                member.getGender(),
                member.getIntroduction(),
                Optional.ofNullable(member.getProfileImage())
                        .map(FileEntity::getEndpoint)
                        .orElse(null)
        );
    }
}
