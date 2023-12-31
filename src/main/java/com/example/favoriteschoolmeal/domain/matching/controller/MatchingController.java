package com.example.favoriteschoolmeal.domain.matching.controller;

import com.example.favoriteschoolmeal.domain.matching.controller.dto.MatchingResponse;
import com.example.favoriteschoolmeal.domain.matching.controller.dto.MemberMatchingCountResponse;
import com.example.favoriteschoolmeal.domain.matching.domain.Matching;
import com.example.favoriteschoolmeal.domain.matching.service.MatchingService;
import com.example.favoriteschoolmeal.global.common.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
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
    public ApiResponse<MatchingResponse> matchingApply(
            @PathVariable final Long postId) {
        MatchingResponse resposne = matchingService.applyMatching(postId);
        return ApiResponse.createSuccess(resposne);
    }

    @DeleteMapping("/posts/{postId}/cancel-application")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ApiResponse<Void> matchingApplicationCancel(
            @PathVariable final Long postId) {
        matchingService.cancelMatchingApplication(postId);
        return ApiResponse.createSuccess(null);
    }

    @PatchMapping("/posts/{postId}/accept-application/{memberId}")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<Void> matchingApplicationAccept(
            @PathVariable final Long postId,
            @PathVariable final Long memberId) {
        matchingService.acceptMatchingApplication(postId, memberId);
        return ApiResponse.createSuccess(null);
    }

    @PatchMapping("/posts/{postId}/reject-application/{memberId}")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<Void> matchingApplicationReject(
            @PathVariable final Long postId,
            @PathVariable final Long memberId) {
        matchingService.rejectMatchingApplication(postId, memberId);
        return ApiResponse.createSuccess(null);
    }

    @PatchMapping("/posts/{postId}/complete-matching")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<Void> matchingComplete(
            @PathVariable final Long postId) {
        matchingService.completeMatching(postId);
        return ApiResponse.createSuccess(null);
    }

    @GetMapping("/members/{memberId}/matching-count")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<MemberMatchingCountResponse> matchingCount(
            @PathVariable final Long memberId) {
        final MemberMatchingCountResponse response = matchingService.countMatching(memberId);
        return ApiResponse.createSuccess(response);
    }
}
