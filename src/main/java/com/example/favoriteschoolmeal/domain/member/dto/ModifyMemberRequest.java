package com.example.favoriteschoolmeal.domain.member.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ModifyMemberRequest(

        @NotBlank
        @Size(min = 3, max = 20, message = "닉네임은 3자리 이상, 20자리 이하여야 합니다.")
        String nickname,

        @NotBlank
        @Size(max = 300)
        String introduction
) {
}
