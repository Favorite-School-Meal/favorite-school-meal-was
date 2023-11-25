package com.example.favoriteschoolmeal.domain.member.api;


import com.example.favoriteschoolmeal.domain.member.dto.MemberDetailResponse;
import com.example.favoriteschoolmeal.domain.member.dto.ModifyMemberRequest;
import com.example.favoriteschoolmeal.domain.member.service.MemberService;
import com.example.favoriteschoolmeal.global.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/v1")
@RequiredArgsConstructor
@RestController
public class MemberController {

    private final MemberService memberService;


    //개인정보수정
    @PutMapping("/member/modify/{memberId}")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<MemberDetailResponse> memberModify(@PathVariable final Long memberId,
                                                          @RequestBody final ModifyMemberRequest modifyMemberRequest) {

        final MemberDetailResponse memberDetailResponse = memberService.modifyMember(modifyMemberRequest, memberId);
        return ApiResponse.createSuccess(memberDetailResponse);
    }

    @GetMapping("/member/{memberId}")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<MemberDetailResponse> memberDetails(@PathVariable final Long memberId){

        final MemberDetailResponse memberDetailResponse = memberService.findMember(memberId);
        return ApiResponse.createSuccess(memberDetailResponse);
    }

    //관리자가 member 모두 불러오기
    @GetMapping("/member")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<?> memberList(){
        return null;
    }
}
