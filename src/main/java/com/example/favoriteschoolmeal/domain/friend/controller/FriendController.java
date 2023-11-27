package com.example.favoriteschoolmeal.domain.friend.controller;

import com.example.favoriteschoolmeal.domain.friend.controller.dto.MemberFriendCountResponse;
import com.example.favoriteschoolmeal.domain.friend.service.FriendService;
import com.example.favoriteschoolmeal.domain.member.dto.PaginatedMemberListResponse;
import com.example.favoriteschoolmeal.global.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class FriendController {

    private final FriendService friendService;

    @PostMapping("/members/{memberId}/request-friend")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<Void> friendRequest(@PathVariable final Long memberId){
        friendService.requestFriend(memberId);
        return ApiResponse.createSuccess(null);
    }

    @DeleteMapping("/members/{memberId}/cancel-friend-request")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ApiResponse<Void> friendRequestCancel(@PathVariable final Long memberId){
        friendService.cancelFriendRequest(memberId);
        return ApiResponse.createSuccess(null);
    }
    @PatchMapping("/members/{memberId}/accept-friend")
    public ApiResponse<Void> friendAccept(@PathVariable final Long memberId){
        friendService.acceptFriendRequest(memberId);
        return ApiResponse.createSuccess(null);
    }

    @PatchMapping("/members/{memberId}/reject-friend")
    public ApiResponse<Void> friendReject(@PathVariable final Long memberId){
        friendService.rejectFriendRequest(memberId);
        return ApiResponse.createSuccess(null);
    }

    @DeleteMapping("/members/{memberId}/delete-friend")
    public ApiResponse<Void> unfriend(@PathVariable final Long memberId){
        friendService.deleteFriend(memberId);
        return ApiResponse.createSuccess(null);
    }

    /**
     * 현재 로그인한 사용자의 친구 목록을 조회한다.
     * */
    @GetMapping("/friends")
    public ApiResponse<PaginatedMemberListResponse> friendList(Pageable pageable){
        PaginatedMemberListResponse response = friendService.findAllFriends(pageable);
        return ApiResponse.createSuccess(response);
    }

    @GetMapping("/members/{memberId}/friend-count")
    public ApiResponse<MemberFriendCountResponse> friendCount(@PathVariable final Long memberId){
        MemberFriendCountResponse response = friendService.countFriend(memberId);
        return ApiResponse.createSuccess(response);
    }

}
