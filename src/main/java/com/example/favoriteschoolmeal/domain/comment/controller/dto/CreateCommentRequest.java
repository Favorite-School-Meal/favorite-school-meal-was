package com.example.favoriteschoolmeal.domain.comment.controller.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateCommentRequest(
        @NotBlank String content) {

}
