package com.example.favoriteschoolmeal.domain.member.controller;


import com.example.favoriteschoolmeal.domain.member.dto.*;
import com.example.favoriteschoolmeal.domain.member.dto.FindUsernameRequest;
import com.example.favoriteschoolmeal.domain.member.dto.MemberDetailResponse;
import com.example.favoriteschoolmeal.domain.member.dto.MemberSimpleResponse;
import com.example.favoriteschoolmeal.domain.member.dto.ModifyMemberRequest;
import com.example.favoriteschoolmeal.domain.member.dto.ModifyPasswordRequest;
import com.example.favoriteschoolmeal.domain.member.dto.PaginatedMemberListResponse;
import com.example.favoriteschoolmeal.domain.member.service.MemberService;
import com.example.favoriteschoolmeal.global.common.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RequestMapping("/api/v1")
@RequiredArgsConstructor
@RestController
public class MemberController {

    private final MemberService memberService;


    //개인정보수정
    //introduction, nickname 수정
    @PutMapping("/members/{memberId}/modify")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<MemberDetailResponse> memberModify(@PathVariable final Long memberId,
                                                          @Valid @RequestBody final ModifyMemberRequest request) {

        final MemberDetailResponse response = memberService.modifyMember(request, memberId);
        return ApiResponse.createSuccess(response);
    }


    //비밀번호 변경은 회원정보 수정에서 분리하여 따로 처리하였습니다.
    @PutMapping("/members/{memberId}/modify-password")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<MemberDetailResponse> memberModifyPassword(@PathVariable final Long memberId,
                                                                  @Valid @RequestBody final ModifyPasswordRequest request) {
        final MemberDetailResponse response = memberService.modifyMemberPassword(request, memberId);
        return ApiResponse.createSuccess(response);
    }


    /**
     * 프로필 이미지 업로드 API
     */
    @PostMapping("/members/{memberId}/profile-image")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<MemberDetailResponse> memberProfileImageSave(
            @PathVariable final Long memberId,
            MultipartFile file) {

        final MemberDetailResponse response = memberService.saveProfileImage(memberId, file);
        return ApiResponse.createSuccess(response);
    }


    @GetMapping("/members/{memberId}")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<MemberDetailResponse> memberDetails(@PathVariable final Long memberId) {

        final MemberDetailResponse response = memberService.findDetailMember(memberId);
        return ApiResponse.createSuccess(response);
    }

    @GetMapping("/members/{memberId}/simple")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<MemberSimpleResponse> memberSimples(@PathVariable final Long memberId) {

        final MemberSimpleResponse response = memberService.findSimpleMember(memberId);
        return ApiResponse.createSuccess(response);
    }

    //현재 로그인된 사용자의 회원 정보 (메서드명이 마음에 안들긴함)
    @GetMapping("/members")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<MemberDetailResponse> memberCurrent() {

        final MemberDetailResponse response = memberService.findCurrentMember();
        return ApiResponse.createSuccess(response);
    }

    //아이디 찾기
    @GetMapping("/members/find-username")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<MemberSimpleResponse> memberFindUsername(
            @Valid @RequestBody final FindUsernameRequest request) {
        final MemberSimpleResponse response = memberService.findUsername(request);
        return ApiResponse.createSuccess(response);
    }


    //관리자가 member 모두 불러오기
    @GetMapping("/admin/members")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<PaginatedMemberListResponse> memberList(Pageable pageable) {

        final PaginatedMemberListResponse response = memberService.findAllMember(pageable);
        return ApiResponse.createSuccess(response);
    }

    /**
     * 관리자가 회원을 정지시키는 메소드
     */
    @PatchMapping("/admin/members/{memberId}/block")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<Void> memberBlock(@PathVariable final Long memberId,
                                         @Valid @RequestBody final BlockRequest blockRequest) {

        memberService.blockMember(memberId, blockRequest);
        return ApiResponse.createSuccess(null);
    }

    /**
     * 관리자가 회원 정지를 해제하는 메소드
     */
    @PatchMapping("/admin/members/{memberId}/unblock")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<Void> memberUnblock(@PathVariable final Long memberId) {

        memberService.unblockMember(memberId);
        return ApiResponse.createSuccess(null);
    }
}
