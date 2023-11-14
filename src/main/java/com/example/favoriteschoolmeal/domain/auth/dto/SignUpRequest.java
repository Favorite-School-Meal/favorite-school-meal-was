package com.example.favoriteschoolmeal.domain.auth.dto;


import com.example.favoriteschoolmeal.domain.model.Gender;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record SignUpRequest(

        @NotBlank
        @Size(min = 3, max = 20, message = "아이디는 3자리 이상, 20자리 이하여야 합니다.")
        String username,

        @NotBlank
        @Size(min = 2, max = 4, message = "이름은 2자리 이상, 4자리 이하여야 합니다.")
        String fullname,

        @NotBlank
        @Size(min = 3, max = 20, message = "닉네임은 3자리 이상, 20자리 이하여야 합니다.")
        String nickname,

        @NotBlank
        @Email(message = "email 형식이여야 합니다.")
        String email,

        @NotBlank
        @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,16}", message = "비밀번호는 8~16자 길이, 영문 대소문자, 숫자, 특수문자를 모두 포함해야 합니다.")
        String password,

        @NotBlank
        Integer age
) {



}
