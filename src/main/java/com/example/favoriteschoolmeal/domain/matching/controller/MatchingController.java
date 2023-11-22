package com.example.favoriteschoolmeal.domain.matching.controller;

import com.example.favoriteschoolmeal.domain.matching.service.MatchingService;
import com.example.favoriteschoolmeal.global.common.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/posts/{postId}")
@RestController
public class MatchingController {

    private final MatchingService matchingService;

    public MatchingController(final MatchingService matchingService) {
        this.matchingService = matchingService;
    }

    @PostMapping("/apply-matching")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<Void> matchingApply(
            @PathVariable final Long postId) {
        matchingService.applyMatching(postId);
        return ApiResponse.createSuccess(null);
    }

    @DeleteMapping("/cancel-application")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ApiResponse<Void> matchingApplicationCancel(
            @PathVariable final Long postId) {
        matchingService.cancelMatchingApplication(postId);
        return ApiResponse.createSuccess(null);
    }

    @PatchMapping("/accept-application/{memberId}")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<Void> matchingApplicationAccept(
            @PathVariable final Long postId,
            @PathVariable final Long memberId) {
        matchingService.acceptMatchingApplication(postId, memberId);
        return ApiResponse.createSuccess(null);
    }

    @PatchMapping("/reject-application/{memberId}")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<Void> matchingApplicationReject(
            @PathVariable final Long postId,
            @PathVariable final Long memberId) {
        matchingService.rejectMatchingApplication(postId, memberId);
        return ApiResponse.createSuccess(null);
    }

    @PatchMapping("/complete-matching")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<Void> matchingComplete(
            @PathVariable final Long postId) {
        matchingService.completeMatching(postId);
        return ApiResponse.createSuccess(null);
    }
}
