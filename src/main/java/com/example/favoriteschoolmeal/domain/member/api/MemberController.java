package com.example.favoriteschoolmeal.domain.member.api;


import com.example.favoriteschoolmeal.domain.member.dto.*;
import com.example.favoriteschoolmeal.domain.member.service.MemberService;
import com.example.favoriteschoolmeal.global.common.response.ApiResponse;
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
    @PutMapping("/members/{memberId}")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<MemberDetailResponse> memberModify(@PathVariable final Long memberId,
                                                          @RequestBody final ModifyMemberRequest request) {

        final MemberDetailResponse response = memberService.modifyMember(request, memberId);
        return ApiResponse.createSuccess(response);
    }

    /**
     * 프로필 이미지 업로드 API
     * */
    @PostMapping("/members/{memberId}/profile-image")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<MemberDetailResponse> memberProfileImageSave(@PathVariable final Long memberId,
                                                                    MultipartFile file) {

        final MemberDetailResponse response = memberService.saveProfileImage(memberId,file);
        return ApiResponse.createSuccess(response);
    }

    @GetMapping("/members/{memberId}")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<MemberDetailResponse> memberDetails(@PathVariable final Long memberId){

        final MemberDetailResponse response = memberService.findDetailMember(memberId);
        return ApiResponse.createSuccess(response);
    }

    @GetMapping("/members/simple/{memberId}")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<MemberSimpleResponse> memberSimples(@PathVariable final Long memberId){

        final MemberSimpleResponse response = memberService.findSimpleMember(memberId);
        return ApiResponse.createSuccess(response);
    }

    //현재 로그인된 사용자의 회원 정보 (메서드명이 마음에 안들긴함)
    @GetMapping("/members")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<MemberDetailResponse> memberCurrent(){

        final MemberDetailResponse response = memberService.findCurrentMember();
        return ApiResponse.createSuccess(response);
    }



    //관리자가 member 모두 불러오기
    @GetMapping("/admin/members")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<PaginatedMemberListResponse> memberList(Pageable pageable){

        final PaginatedMemberListResponse response = memberService.findAllMember(pageable);
        return ApiResponse.createSuccess(response);
    }

    /**
     * 관리자가 회원을 정지시키는 메소드
     */
    @PatchMapping("/admin/members/{memberId}/block")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<Void> memberBlock(@PathVariable final Long memberId,
                                                         @RequestBody final BlockRequest blockRequest){

        memberService.blockMember(memberId, blockRequest);
        return ApiResponse.createSuccess(null);
    }

    /**
     * 관리자가 회원 정지를 해제하는 메소드
     */
    @PatchMapping("/admin/members/{memberId}/unblock")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<Void> memberUnblock(@PathVariable final Long memberId){

        memberService.unblockMember(memberId);
        return ApiResponse.createSuccess(null);
    }
}
