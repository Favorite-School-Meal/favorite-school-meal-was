package com.example.favoriteschoolmeal.domain.post.controller.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record CreatePostRequest(
        @NotBlank String title,
        @NotBlank String content,
        @NotNull String meetingDateTime,
        @NotNull @Min(2) Integer maxParticipant) {

}
