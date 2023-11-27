package com.example.favoriteschoolmeal.domain.friend.controller;

import com.example.favoriteschoolmeal.domain.friend.service.FriendService;
import com.example.favoriteschoolmeal.global.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class FriendController {

    private final FriendService friendService;

    @PostMapping("member/{memberId}/request-friend")
    public ApiResponse<Void> friendRequest(@PathVariable final Long memberId){
        friendService.requestFriend(memberId);
        return ApiResponse.createSuccess(null);
    }

    @DeleteMapping("member/{memberId}/cancel-friend-request")
    public ApiResponse<Void> friendRequestCancel(@PathVariable final Long memberId){
        friendService.cancelFriendRequest(memberId);
        return ApiResponse.createSuccess(null);
    }
    @PatchMapping("member/{memberId}/friend-accept")
    public ApiResponse<Void> friendAccept(@PathVariable final Long memberId){
        return ApiResponse.createSuccess(null);
    }

    @PatchMapping("member/{memberId}/friend-reject")
    public ApiResponse<Void> friendReject(@PathVariable final Long memberId){
        return ApiResponse.createSuccess(null);
    }

    @GetMapping("member/{memberId}/friends")
    public ApiResponse<Void> friendList(@PathVariable final Long memberId){
        return ApiResponse.createSuccess(null);
    }

}
