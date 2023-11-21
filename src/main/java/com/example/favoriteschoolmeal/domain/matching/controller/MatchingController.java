package com.example.favoriteschoolmeal.domain.matching.controller;

import com.example.favoriteschoolmeal.domain.matching.service.MatchingService;
import com.example.favoriteschoolmeal.global.common.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1")
@RestController
public class MatchingController {

    private final MatchingService matchingService;

    public MatchingController(final MatchingService matchingService) {
        this.matchingService = matchingService;
    }

    @PostMapping("/posts/{postId}/apply-matching")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<Void> matchingApply(
            @PathVariable final Long postId) {
        matchingService.applyMatching(postId);
        return ApiResponse.createSuccess(null);
    }

    @DeleteMapping("/posts/{postId}/cancel-application")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ApiResponse<Void> matchingApplicationCancel(
            @PathVariable final Long postId) {
        matchingService.cancelMatchingApplication(postId);
        return ApiResponse.createSuccess(null);
    }

    // TODO: Host가 매칭 승인

    // TODO: Host가 매칭 거절

    // TODO: Host가 매칭 상태(모집 중, 모집 완료) 관리
}
