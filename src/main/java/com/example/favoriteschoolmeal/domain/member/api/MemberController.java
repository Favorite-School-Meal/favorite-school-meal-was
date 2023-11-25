package com.example.favoriteschoolmeal.domain.member.api;


import com.example.favoriteschoolmeal.domain.member.dto.MemberDetailResponse;
import com.example.favoriteschoolmeal.domain.member.dto.ModifyMemberRequest;
import com.example.favoriteschoolmeal.domain.member.dto.PaginatedMemberListResponse;
import com.example.favoriteschoolmeal.domain.member.service.MemberService;
import com.example.favoriteschoolmeal.global.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/v1")
@RequiredArgsConstructor
@RestController
public class MemberController {

    private final MemberService memberService;


    //개인정보수정
    @PutMapping("/member/{memberId}")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<MemberDetailResponse> memberModify(@PathVariable final Long memberId,
                                                          @RequestBody final ModifyMemberRequest request) {

        final MemberDetailResponse response = memberService.modifyMember(request, memberId);
        return ApiResponse.createSuccess(response);
    }

    @GetMapping("/member/{memberId}")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<MemberDetailResponse> memberDetails(@PathVariable final Long memberId){

        final MemberDetailResponse response = memberService.findMember(memberId);
        return ApiResponse.createSuccess(response);
    }

    //관리자가 member 모두 불러오기
    @GetMapping("/admin/member")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<PaginatedMemberListResponse> memberList(Pageable pageable){

        final PaginatedMemberListResponse response = memberService.findAllMember(pageable);
        return ApiResponse.createSuccess(response);
    }
}
