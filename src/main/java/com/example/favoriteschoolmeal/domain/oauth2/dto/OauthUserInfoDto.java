package com.example.favoriteschoolmeal.domain.oauth2.dto;


import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class OauthUserInfoDto {

    private String platformId;
    private String fullname;
    private String nickname;
    private String email;
    private String gender;
    private String age;
    private String birthday;
}
